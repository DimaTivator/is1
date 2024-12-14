package org.dimativator.is1.service;

import lombok.RequiredArgsConstructor;
import org.apache.hadoop.conf.Configuration;
import org.apache.parquet.hadoop.ParquetReader;
import org.dimativator.is1.model.Person;
import org.dimativator.is1.model.Coordinates;
import org.dimativator.is1.model.Location;
import org.dimativator.is1.model.User;
import org.dimativator.is1.repository.PersonRepository;
import org.dimativator.is1.repository.CoordinatesRepository;
import org.dimativator.is1.repository.LocationRepository;
import org.dimativator.is1.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParquetImportService {
    
    private final PersonRepository personRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final LocationRepository locationRepository;
    private final ImportHistoryService importHistoryService;

    @Transactional
    public void importPeopleFromParquet(MultipartFile file, User user) throws Exception {
        boolean success = true;
        int rowCount = 0;
        try {
            // Create temporary file
            File tempFile = File.createTempFile("upload", ".parquet");
            file.transferTo(tempFile);

            Configuration conf = new Configuration();
            List<Person> people = new ArrayList<>();

            try (ParquetReader<Person> reader = ParquetReader.builder(new PersonParquetReadSupport(), 
                    new org.apache.hadoop.fs.Path(tempFile.getAbsolutePath()))
                    .withConf(conf)
                    .build()) {

                Person person;
                while ((person = reader.read()) != null) {
                    rowCount++;
                    // Check if coordinates already exist
                    Coordinates coordinates = person.getCoordinates();
                    Coordinates existingCoordinates = coordinatesRepository
                        .findByXAndY(coordinates.getX(), coordinates.getY())
                        .orElseGet(() -> coordinatesRepository.save(coordinates));
                    person.setCoordinates(existingCoordinates);
                    
                    // Check if location already exists
                    Location location = person.getLocation();
                    Location existingLocation = locationRepository
                        .findByXAndYAndZ(location.getX(), location.getY(), location.getZ())
                        .orElseGet(() -> locationRepository.save(location));
                    person.setLocation(existingLocation);
                    
                    // Set the user for this person
                    person.setUser(user);
                    
                    people.add(person);
                }
            }

            personRepository.saveAll(people);
            tempFile.delete();
        } catch (Exception e) {
            success = false;
            throw e;
        } finally {
            importHistoryService.recordImport(
                user.getLogin(),
                file.getOriginalFilename(),
                success ? rowCount : 0,
                success
            );
        }
    }
} 