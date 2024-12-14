package org.dimativator.is1.service;

import lombok.RequiredArgsConstructor;
import org.apache.hadoop.conf.Configuration;
import org.apache.parquet.hadoop.ParquetReader;
import org.dimativator.is1.model.Person;
import org.dimativator.is1.mappers.PersonMapper;
import org.dimativator.is1.model.Coordinates;
import org.dimativator.is1.model.Location;
import org.dimativator.is1.model.User;
import org.dimativator.is1.repository.PersonRepository;
import org.dimativator.is1.services.PersonService;
import org.dimativator.is1.repository.CoordinatesRepository;
import org.dimativator.is1.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParquetImportService {

    private final PersonService personService;
    
    private final PersonRepository personRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final LocationRepository locationRepository;
    private final ImportHistoryService importHistoryService;
    private final TransactionCoordinator transactionCoordinator;

    @Transactional
    public void importPeopleFromParquet(MultipartFile file, User user) throws Exception {
        TransactionOperation<ImportResult> operation = new TransactionOperation<>() {
            private List<Person> people;
            private int rowCount;

            @Override
            public ImportResult prepare() throws Exception {
                File tempFile = File.createTempFile("upload", ".parquet");
                file.transferTo(tempFile);
                people = new ArrayList<>();
                rowCount = 0;

                try (ParquetReader<Person> reader = ParquetReader.builder(new PersonParquetReadSupport(), 
                        new org.apache.hadoop.fs.Path(tempFile.getAbsolutePath()))
                        .withConf(new Configuration())
                        .build()) {

                    Person person;
                    while ((person = reader.read()) != null) {
                        rowCount++;
                        processAndValidatePerson(person, user);
                        people.add(person);
                    }
                }

                tempFile.delete();
                return new ImportResult(rowCount, people);
            }

            @Override
            public void commit() {
                personRepository.saveAll(people);
            }

            @Override
            public void rollback() {
                // Transaction will be rolled back automatically
            }
        };

        try {
            TransactionResult<ImportResult> result = transactionCoordinator.executeTransaction(file, operation);
            importHistoryService.recordImport(
                user.getLogin(),
                file.getOriginalFilename(),
                result.getMinioFilename(),
                result.getResult().getRowCount(),
                true
            );
        } catch (ResponseStatusException e) {
            throw e;
        } catch (RuntimeException e) {
            if (e.getCause() instanceof ResponseStatusException) {
                throw (ResponseStatusException) e.getCause();
            }
            importHistoryService.recordImport(
                user.getLogin(),
                file.getOriginalFilename(),
                "", 
                0,
                false
            );
            throw e;
        }
    }

    private void processAndValidatePerson(Person person, User user) {
        Coordinates coordinates = person.getCoordinates();
        Coordinates existingCoordinates = coordinatesRepository
            .findByXAndY(coordinates.getX(), coordinates.getY())
            .orElseGet(() -> coordinatesRepository.save(coordinates));
        person.setCoordinates(existingCoordinates);
        
        Location location = person.getLocation();
        Location existingLocation = locationRepository
            .findByXAndYAndZ(location.getX(), location.getY(), location.getZ())
            .orElseGet(() -> locationRepository.save(location));
        person.setLocation(existingLocation);
        
        person.setUser(user);
        personService.checkUniqueCombination(PersonMapper.toDto(person));
    }
}

@lombok.Value
class ImportResult {
    int rowCount;
    List<Person> people;
} 