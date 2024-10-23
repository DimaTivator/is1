package org.dimativator.is1.mappers;
import org.dimativator.is1.dto.CoordinatesDto;
import org.dimativator.is1.model.Coordinates;

public class CoordinatesMapper {
    public static Coordinates toEntity(CoordinatesDto coordinatesDto) {
        return Coordinates.builder()
                .id(coordinatesDto.getId())
                .x(coordinatesDto.getX())
                .y(coordinatesDto.getY())
                .build();
    }

    public static CoordinatesDto toDto(Coordinates coordinates) {
        return CoordinatesDto.builder()
                .id(coordinates.getId())
                .x(coordinates.getX())
                .y(coordinates.getY())
                .build();
    }
}
