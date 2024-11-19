package org.dimativator.is1.services;

import org.dimativator.is1.dto.CoordinatesDto;
import org.dimativator.is1.model.Coordinates;

import java.util.List;
import java.util.Optional;

public interface CoordinatesService {
    List<CoordinatesDto> getAllCoordinates();
    void saveCoordinates(CoordinatesDto coordinatesDto);
    Optional<Coordinates> getCoordinatesByXAndY(Long x, Double y);
}
