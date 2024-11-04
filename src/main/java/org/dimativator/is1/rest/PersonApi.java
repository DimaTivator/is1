package org.dimativator.is1.rest;

import org.dimativator.is1.dto.PersonDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface PersonApi {
    @GetMapping("/persons")
    ResponseEntity<List<PersonDto>> getAllPersons();

    @PostMapping(value = "/persons", consumes = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    ResponseEntity<Void> savePerson(PersonDto personDto);

    @GetMapping(value = "/persons/eye-color", consumes = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    ResponseEntity<List<PersonDto>> getPersonsByEyeColor(@RequestParam String color);

    @GetMapping(value = "/persons/hair-color", consumes = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    ResponseEntity<List<PersonDto>> getPersonsByHairColor(@RequestParam String color);

    @GetMapping(value = "/persons/nationality-less", consumes = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    ResponseEntity<List<PersonDto>> getPersonsWithNationalityLessThan(@RequestParam String country);

    @GetMapping(value = "/persons/nationality-greater", consumes = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    ResponseEntity<List<PersonDto>> getPersonsWithNationalityGreaterThan(@RequestParam String country);

    @DeleteMapping(value = "/persons/height", consumes = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    ResponseEntity<Void> deletePersonsByHeight(@RequestParam float height);

    @DeleteMapping(value = "/persons/id", consumes = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    ResponseEntity<Void> deletePersonById(@RequestParam Long id);
}
