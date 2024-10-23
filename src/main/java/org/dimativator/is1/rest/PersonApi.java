package org.dimativator.is1.rest;

import org.dimativator.is1.dto.PersonDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/v1")
public interface PersonApi {
    @GetMapping("/persons")
    ResponseEntity<List<PersonDto>> getAllPersons();
}
