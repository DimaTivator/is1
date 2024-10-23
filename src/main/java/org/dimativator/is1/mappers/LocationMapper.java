package org.dimativator.is1.mappers;

import org.dimativator.is1.dto.LocationDto;
import org.dimativator.is1.model.Location;

public class LocationMapper {
    public static Location toEntity(LocationDto locationDto) {
        return Location.builder()
                .id(locationDto.getId())
                .x(locationDto.getX())
                .y(locationDto.getY())
                .z(locationDto.getZ())
                .build();
    }

    public static LocationDto toDto(Location location) {
        return LocationDto.builder()
                .id(location.getId())
                .x(location.getX())
                .y(location.getY())
                .z(location.getZ())
                .build();
    }
}
