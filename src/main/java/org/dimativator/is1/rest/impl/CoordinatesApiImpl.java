package org.dimativator.is1.rest.impl;

import org.dimativator.is1.dto.CoordinatesDto;
import org.dimativator.is1.rest.CoordinatesApi;
import org.dimativator.is1.services.CoordinatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CoordinatesApiImpl implements CoordinatesApi {
    private final CoordinatesService coordinatesService;

    @Autowired
    public CoordinatesApiImpl(CoordinatesService coordinatesService) {
        this.coordinatesService = coordinatesService;
    }

    @Override
    public ResponseEntity<List<CoordinatesDto>> getAllCoordinates() {
        return ResponseEntity.ok(coordinatesService.getAllCoordinates());
    }

    @Override
    public ResponseEntity<Void> saveCoordinates(CoordinatesDto coordinatesDto) {
        coordinatesService.saveCoordinates(coordinatesDto);
        return ResponseEntity.ok().build();
    }
}
