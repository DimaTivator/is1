package org.dimativator.is1.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "is1_locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "x")
    private Long x;

    @Column(name = "y")
    private Float y;

    @Column(name = "z", nullable = false)
    private Float z;
}
