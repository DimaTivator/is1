package org.dimativator.is1.repository;

import org.dimativator.is1.model.*;

public record PersonFilter(
        Long id,
        String name,
        Coordinates coordinates,
        java.time.LocalDateTime creationDate,
        Color eyeColor,
        Color hairColor,
        Location location,
        Float height,
        java.time.ZonedDateTime birthday,
        Country nationality,
        User user
) {
}
