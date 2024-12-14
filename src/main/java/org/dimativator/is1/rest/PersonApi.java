package org.dimativator.is1.rest;

import org.dimativator.is1.dto.PersonDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import java.util.List;

public interface PersonApi {
    @GetMapping("/persons")
    ResponseEntity<List<PersonDto>> getAllPersons(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                  @RequestParam(defaultValue = "5") @Min(0) int limit,
                                                  @RequestParam(value = "id", required = false) Long id,
                                                  @RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "coordinates", required = false) String coordinates,
                                                  @RequestParam(value = "creationDate", required = false) Long creationDateTimestampMs,
                                                  @RequestParam(value = "eyeColor", required = false) String eyeColor,
                                                  @RequestParam(value = "hairColor", required = false) String hairColor,
                                                  @RequestParam(value = "location", required = false) String location,
                                                  @RequestParam(value = "height", required = false) Float height,
                                                  @RequestParam(value = "birthday", required = false) Long birthdayTimestampMs,
                                                  @RequestParam(value = "nationality", required = false) String nationality,
                                                  @RequestParam(value = "login", required = false) String login,
                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                  @RequestParam(defaultValue = "true") boolean ascending);


    @PostMapping(value = "/persons", consumes = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    ResponseEntity<PersonDto> createPerson(@RequestBody PersonDto personDto, @RequestHeader(name = "Authorization") String token);

    @PutMapping(value = "/persons", consumes = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    ResponseEntity<PersonDto> updatePerson(@RequestBody PersonDto personDto, @RequestHeader(name = "Authorization") String token);

    @GetMapping(value = "/persons/count-eye-color", consumes = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    ResponseEntity<Integer> countPersonsByEyeColor(@RequestParam String color);

    @GetMapping(value = "/persons/count-hair-color", consumes = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    ResponseEntity<Integer> countPersonsByHairColor(@RequestParam String color);

    @GetMapping(value = "/persons/count-nationality-less", consumes = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    ResponseEntity<Integer> countPersonsWithNationalityLessThan(@RequestParam String country);

    @GetMapping(value = "/persons/count-nationality-equal", consumes = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    ResponseEntity<Integer> countPersonsWithNationalityEqualTo(@RequestParam String country);

    @DeleteMapping(value = "/persons/height", consumes = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    ResponseEntity<Void> deletePersonsByHeight(@RequestParam float height, @RequestHeader(name = "Authorization") String token);

    @DeleteMapping(value = "/persons/id", consumes = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    ResponseEntity<Void> deletePersonById(@RequestParam Long id, @RequestHeader(name = "Authorization") String token);

    @PostMapping("/persons/import-parquet")
    ResponseEntity<String> importParquet(@RequestParam("file") MultipartFile file, 
                                        @RequestHeader(name = "Authorization") String token);
}
