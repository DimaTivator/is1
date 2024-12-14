package org.dimativator.is1.rest.impl;

import lombok.RequiredArgsConstructor;
import org.dimativator.is1.dto.ImportHistoryDto;
import org.dimativator.is1.model.Role;
import org.dimativator.is1.model.User;
import org.dimativator.is1.rest.ImportHistoryApi;
import org.dimativator.is1.service.ImportHistoryService;
import org.dimativator.is1.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImportHistoryApiImpl implements ImportHistoryApi {
    
    private final ImportHistoryService importHistoryService;
    private final UserService userService;

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

    private String getToken(String bearerToken) {
        return bearerToken.split(" ")[1];
    }
} 