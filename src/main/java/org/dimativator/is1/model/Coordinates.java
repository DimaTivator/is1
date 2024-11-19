package org.dimativator.is1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "is1_coordinates", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"x", "y"})
})
public class Coordinates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Max(128)
    @Column(name = "x")
    private Long x;

    @Min(-436)
    @Column(name = "y")
    private Double y;
}
