package org.dimativator.is1.dto;

import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class ImportHistoryDto {
    private Long id;
    private String username;
    private String filename;
    private String minioFilename;
    private Integer rowCount;
    private Boolean success;
    private ZonedDateTime importDate;
} 