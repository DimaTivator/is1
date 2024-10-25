package org.dimativator.is1.rest;

import org.dimativator.is1.dto.CoordinatesDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CoordinatesApi {
    @GetMapping("/coordinates")
    ResponseEntity<List<CoordinatesDto>> getAllCoordinates();

    @PostMapping(value = "/coordinates", consumes = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    ResponseEntity<Void> saveCoordinates(@RequestBody CoordinatesDto coordinatesDto);
}
