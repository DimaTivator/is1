package org.dimativator.is1.model;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coordinates_id", nullable = false)
    private Coordinates coordinates;

    @Column(name = "creation_date", nullable = false)
    @PrePersist
    protected void onCreate() {
        this.creationDate = java.time.LocalDateTime.now();
    }
    private java.time.LocalDateTime creationDate;

    @Column(name = "eye_color", nullable = false)
    @Enumerated(EnumType.STRING)
    private Color eyeColor;

    @Column(name = "hair_color", nullable = false)
    @Enumerated(EnumType.STRING)
    private Color hairColor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Min(0)
    @Column(name = "height")
    private Float height;

    @Column(name = "birthday", nullable = false)
    private java.time.ZonedDateTime birthday;

    @Column(name = "nationality")
    @Enumerated(EnumType.STRING)
    private Country nationality;
}
