package org.dimativator.is1.rest.impl;

import org.dimativator.is1.dto.LocationDto;
import org.dimativator.is1.rest.LocationApi;
import org.dimativator.is1.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class LocationApiImpl implements LocationApi {
    private final LocationService locationService;

    @Autowired
    public LocationApiImpl(LocationService locationService) {
        this.locationService = locationService;
    }

    @Override
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    @Override
    public ResponseEntity<Void> saveLocation(LocationDto locationDto) {
        locationService.saveLocation(locationDto);
        return ResponseEntity.ok().build();
    }
}
