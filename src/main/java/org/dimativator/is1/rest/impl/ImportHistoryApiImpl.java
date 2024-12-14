package org.dimativator.is1.rest.impl;

import lombok.RequiredArgsConstructor;
import org.dimativator.is1.dto.ImportHistoryDto;
import org.dimativator.is1.model.ImportHistory;
import org.dimativator.is1.model.Role;
import org.dimativator.is1.model.User;
import org.dimativator.is1.rest.ImportHistoryApi;
import org.dimativator.is1.service.ImportHistoryService;
import org.dimativator.is1.service.MinioService;
import org.dimativator.is1.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.InputStream;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImportHistoryApiImpl implements ImportHistoryApi {
    
    private final ImportHistoryService importHistoryService;
    private final UserService userService;
    private final MinioService minioService;

    @Override
    public ResponseEntity<List<ImportHistoryDto>> getImportHistory(String token) {
        User user = userService.getUserByToken(getToken(token));
        return ResponseEntity.ok(importHistoryService.getUserHistory(user));
    }

    @Override
    public ResponseEntity<List<ImportHistoryDto>> getAllImportHistory(String token) {
        User user = userService.getUserByToken(getToken(token));
        if (user.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can view all import history");
        }
        return ResponseEntity.ok(importHistoryService.getAllHistory());
    }

    @Override
    public ResponseEntity<Resource> downloadImportFile(Long id, String token) {
        User user = userService.getUserByToken(getToken(token));
        ImportHistory history = importHistoryService.getById(id);
        
        // Check permissions
        if (!user.getRole().equals(Role.ADMIN) && !history.getUsername().equals(user.getLogin())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        
        try {
            InputStream fileStream = minioService.downloadFile(history.getMinioFilename());
            InputStreamResource resource = new InputStreamResource(fileStream);
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                    "attachment; filename=\"" + history.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error downloading file: " + e.getMessage());
        }
    }

    private String getToken(String bearerToken) {
        return bearerToken.split(" ")[1];
    }
} 