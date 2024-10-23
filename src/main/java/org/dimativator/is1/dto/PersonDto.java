package org.dimativator.is1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.dimativator.is1.model.Color;
import org.dimativator.is1.model.Country;

@Data
@Builder
@AllArgsConstructor
public class PersonDto {
    private Long id;
    private String name;
    private CoordinatesDto coordinates;
    private java.time.LocalDateTime creationDate;
    private Color eyeColor;
    private Color hairColor;
    private LocationDto location;
    private Float height;
    private java.time.ZonedDateTime birthday;
    private Country nationality;
}
