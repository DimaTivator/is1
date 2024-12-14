package org.dimativator.is1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "is1_import_history")
public class ImportHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String minioFilename;

    @Column(nullable = false)
    private Integer rowCount;

    @Column(nullable = false)
    private Boolean success;

    @Column(nullable = false)
    private ZonedDateTime importDate;
} 