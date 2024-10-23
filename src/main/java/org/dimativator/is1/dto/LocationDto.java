package org.dimativator.is1.dto;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    private Long id;
    private Long x;
    private Float y;
    private Float z;
}
