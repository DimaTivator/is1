package org.dimativator.is1.rest.impl;

import org.dimativator.is1.dto.PersonDto;
import org.dimativator.is1.model.Color;
import org.dimativator.is1.model.Country;
import org.dimativator.is1.rest.PersonApi;
import org.dimativator.is1.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonApiImpl implements PersonApi {
    private final PersonService personService;

    @Autowired
    public PersonApiImpl(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public ResponseEntity<List<PersonDto>> getAllPersons() {
        return ResponseEntity.ok(personService.getAllPersons());
    }

    @Override
    public ResponseEntity<Void> savePerson(PersonDto personDto) {
        personService.savePerson(personDto);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<PersonDto>> getPersonsByEyeColor(String color) {
        return ResponseEntity.ok(personService.getPersonsByEyeColor(Color.valueOf(color)));
    }

    @Override
    public ResponseEntity<List<PersonDto>> getPersonsByHairColor(String color) {
        return ResponseEntity.ok(personService.getPersonsByHairColor(Color.valueOf(color)));
    }

    @Override
    public ResponseEntity<List<PersonDto>> getPersonsWithNationalityLessThan(String country) {
        return ResponseEntity.ok(personService.getPersonsWithNationalityLessThan(Country.valueOf(country)));
    }

    @Override
    public ResponseEntity<List<PersonDto>> getPersonsWithNationalityGreaterThan(String country) {
        return ResponseEntity.ok(personService.getPersonsWithNationalityGreaterThan(Country.valueOf(country)));
    }

    @Override
    public ResponseEntity<Void> deletePersonsByHeight(float height) {
        personService.deletePersonsByHeight(height);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deletePersonById(Long id) {
        personService.deletePersonById(id);
        return ResponseEntity.ok().build();
    }
}
