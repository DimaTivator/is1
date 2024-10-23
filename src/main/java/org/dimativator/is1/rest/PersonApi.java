package org.dimativator.is1.rest;

import org.dimativator.is1.dto.PersonDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface PersonApi {
    @GetMapping("/persons")
    ResponseEntity<List<PersonDto>> getAllPersons();
}
