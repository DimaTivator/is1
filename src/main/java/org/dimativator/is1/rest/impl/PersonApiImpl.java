package org.dimativator.is1.rest.impl;

import org.dimativator.is1.dto.PersonDto;
import org.dimativator.is1.mappers.UserMapper;
import org.dimativator.is1.model.*;
import org.dimativator.is1.repository.PersonFilter;
import org.dimativator.is1.rest.PersonApi;
import org.dimativator.is1.service.ParquetImportService;
import org.dimativator.is1.services.CoordinatesService;
import org.dimativator.is1.services.LocationService;
import org.dimativator.is1.services.PersonService;
import org.dimativator.is1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
public class PersonApiImpl implements PersonApi {
    private final PersonService personService;
    private final CoordinatesService coordinatesService;
    private final LocationService locationService;
    private final UserService userService;
    private final ParquetImportService parquetImportService;

    @Autowired
    public PersonApiImpl(PersonService personService,
                         CoordinatesService coordinatesService,
                         LocationService locationService,
                         UserService userService,
                         ParquetImportService parquetImportService) {
        this.personService = personService;
        this.coordinatesService = coordinatesService;
        this.locationService = locationService;
        this.userService = userService;
        this.parquetImportService = parquetImportService;
    }

    @Override
    public ResponseEntity<List<PersonDto>> getAllPersons(int page,
                                                         int limit,
                                                         Long id,
                                                         String name,
                                                         String coordinates,
                                                         Long creationDateTimestampMs,
                                                         String eyeColor,
                                                         String hairColor,
                                                         String location,
                                                         Float height,
                                                         Long birthdayTimestampMs,
                                                         String nationality,
                                                         String login,
                                                         String sortBy,
                                                         boolean ascending) {
        final PersonFilter personFilter = new PersonFilter(
                id,
                name,
                coordinates != null ? getCoordinatesByString(coordinates) : null,
                creationDateTimestampMs != null ? getLocalDateTimeByTimestampMs(creationDateTimestampMs) : null,
                eyeColor != null ? Color.valueOf(eyeColor.toUpperCase()) : null,
                hairColor != null ? Color.valueOf(hairColor.toUpperCase()) : null,
                location != null ? getLocationByString(location) : null,
                height,
                birthdayTimestampMs != null ? getZonedDateTimeByTimestampMs(birthdayTimestampMs) : null,
                nationality != null ? Country.valueOf(nationality.toUpperCase()) : null,
                userService.getUserByLogin(login)
        );

        final Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        return ResponseEntity.ok(personService.getAllPersons(PageRequest.of(page, limit, sort), personFilter));
    }

    @Override
    public ResponseEntity<PersonDto> createPerson(PersonDto personDto, String token) {
        User user = userService.getUserByToken(getToken(token));
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(personService.createPerson(personDto, user));
        } catch (ResponseStatusException e) {
            if (e.getReason() == "Person with this combination of Name, Birthday and Nationality already exists") {
                return ResponseEntity.status(410).body(null);    
            }
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Override
    public ResponseEntity<PersonDto> updatePerson(PersonDto personDto, String token) {
        final User user = userService.getUserByToken(getToken(token));
        personDto.setUser(UserMapper.toDto(user));
        personService.checkUser(personDto.getId(), user);

        try {
            return ResponseEntity.ok(personService.updatePerson(personDto));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Override
    public ResponseEntity<Integer> countPersonsByEyeColor(String color) {
        return ResponseEntity.ok(personService.countPersonsByEyeColor(Color.valueOf(color)));
    }

    @Override
    public ResponseEntity<Integer> countPersonsByHairColor(String color) {
        return ResponseEntity.ok(personService.countPersonsByHairColor(Color.valueOf(color)));
    }

    @Override
    public ResponseEntity<Integer> countPersonsWithNationalityLessThan(String country) {
        return ResponseEntity.ok(personService.countPersonsWithNationalityLessThan(Country.valueOf(country)));
    }

    @Override
    public ResponseEntity<Integer> countPersonsWithNationalityEqualTo(String country) {
        return ResponseEntity.ok(personService.countPersonsWithNationalityEqualTo(Country.valueOf(country)));
    }

    @Override
    public ResponseEntity<Void> deletePersonsByHeight(float height, String token) {
        final User user = userService.getUserByToken(getToken(token));
        personService.deletePersonsByHeight(height, user);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deletePersonById(Long id, String token) {
        final User user = userService.getUserByToken(getToken(token));
        personService.checkUser(id, user);
        personService.deletePersonById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<String> importParquet(@RequestParam("file") MultipartFile file, 
                                              @RequestHeader(name = "Authorization") String token) {
        try {
            User user = userService.getUserByToken(getToken(token));
            parquetImportService.importPeopleFromParquet(file, user);
            return ResponseEntity.ok("Import successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Import failed: " + e.getMessage());
        }
    }

    private String getToken(String bearerToken) {
        return bearerToken.split(" ")[1];
    }

    private Coordinates getCoordinatesByString(String coordinates) {
        coordinates = coordinates.replaceAll("[()]", "");
        String[] coordinatesArray = coordinates.split(", ");
        Long x = Long.parseLong(coordinatesArray[0]);
        Double y = Double.parseDouble(coordinatesArray[1]);
        return coordinatesService.getCoordinatesByXAndY(x, y).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Coordinates not found")
        );
    }

    private Location getLocationByString(String location) {
        location = location.replaceAll("[()]", "");
        String[] locationArray = location.split(", ");
        Long x = Long.parseLong(locationArray[0]);
        Float y = Float.parseFloat(locationArray[1]);
        Float z = Float.parseFloat(locationArray[2]);
        return locationService.getLocationByXAndYAndZ(x, y, z).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location not found")
        );
    }

    private ZonedDateTime getZonedDateTimeByTimestampMs(Long timestampMs) {
        return Instant.ofEpochMilli(timestampMs).atZone(ZoneId.of("UTC"));
    }

    private LocalDateTime getLocalDateTimeByTimestampMs(Long timestampMs) {
        return Instant.ofEpochMilli(timestampMs)
                .atZone(ZoneId.of("UTC"))
                .toLocalDateTime();
    }
}
