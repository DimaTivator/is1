package org.dimativator.is1.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoordinatesDto {
    private Long id;
    private Long x;
    private Double y;
}
