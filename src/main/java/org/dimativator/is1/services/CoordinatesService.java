package org.dimativator.is1.services;

import org.dimativator.is1.dto.CoordinatesDto;

import java.util.List;

public interface CoordinatesService {
    List<CoordinatesDto> getAllCoordinates();
    void saveCoordinates(CoordinatesDto coordinatesDto);
}
