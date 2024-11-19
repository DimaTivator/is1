package org.dimativator.is1.services;

import org.dimativator.is1.dto.LocationDto;
import org.dimativator.is1.model.Location;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    List<LocationDto> getAllLocations();

    void saveLocation(LocationDto locationDto);

    Optional<Location> getLocationByXAndYAndZ(Long x, Float y, Float z);
}
