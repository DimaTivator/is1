package org.dimativator.is1.services.impl;

import org.dimativator.is1.dto.LocationDto;
import org.dimativator.is1.mappers.LocationMapper;
import org.dimativator.is1.repository.LocationRepository;
import org.dimativator.is1.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<LocationDto> getAllLocations() {
        return locationRepository.findAll()
                .stream()
                .map(LocationMapper::toDto)
                .toList();
    }

    @Override
    public void saveLocation(LocationDto locationDto) {
        locationRepository.save(LocationMapper.toEntity(locationDto));
    }
}
