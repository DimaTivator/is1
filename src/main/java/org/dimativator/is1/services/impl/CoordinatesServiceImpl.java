package org.dimativator.is1.services.impl;

import org.dimativator.is1.dto.CoordinatesDto;
import org.dimativator.is1.mappers.CoordinatesMapper;
import org.dimativator.is1.model.Coordinates;
import org.dimativator.is1.repository.CoordinatesRepository;
import org.dimativator.is1.services.CoordinatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoordinatesServiceImpl implements CoordinatesService {
    private final CoordinatesRepository coordinatesRepository;

    @Autowired
    public CoordinatesServiceImpl(CoordinatesRepository coordinatesRepository) {
        this.coordinatesRepository = coordinatesRepository;
    }

    @Override
    public List<CoordinatesDto> getAllCoordinates() {
        return coordinatesRepository.findAll()
                .stream()
                .map(CoordinatesMapper::toDto)
                .toList();
    }

    @Override
    public void saveCoordinates(CoordinatesDto coordinatesDto) {
        coordinatesRepository.save(CoordinatesMapper.toEntity(coordinatesDto));
    }

    @Override
    public Optional<Coordinates> getCoordinatesByXAndY(Long x, Double y) {
        return coordinatesRepository.findByXAndY(x, y);
    }
}
