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
@Table(name = "is1_persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Coordinates coordinates;

    @Column(nullable = false)
    @PrePersist
    protected void onCreate() {
        this.creationDate = java.time.LocalDateTime.now();
    }
    private java.time.LocalDateTime creationDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Color eyeColor;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Color hairColor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Location location;

    @Min(0)
    @Column
    private Float height;

    @Column(nullable = false)
    private java.time.ZonedDateTime birthday;

    @Column
    @Enumerated(EnumType.STRING)
    private Country nationality;

    @JoinColumn
    @ManyToOne
    private User user;
}
