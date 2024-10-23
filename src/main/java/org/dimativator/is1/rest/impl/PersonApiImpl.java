package org.dimativator.is1.rest.impl;

import org.dimativator.is1.dto.PersonDto;
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
}
