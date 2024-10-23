package org.dimativator.is1.rest.impl;

import org.dimativator.is1.dto.UserDto;
import org.dimativator.is1.model.Role;
import org.dimativator.is1.rest.AdminApi;
import org.dimativator.is1.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminApiImpl implements AdminApi {
    private final AdminService adminService;

    @Autowired
    public AdminApiImpl(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public ResponseEntity<List<UserDto>> getAllPotentialAdmins(int page, int limit) {
        return ResponseEntity.ok(adminService.getAllPotentialAdmins(PageRequest.of(page, limit)));
    }

    @Override
    public ResponseEntity<Void> addAdmin(Long id) {
        adminService.setNewRoleToPotentialAdmin(id, Role.ADMIN);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<Void> addUser(Long id) {
        adminService.setNewRoleToPotentialAdmin(id, Role.USER);
        return ResponseEntity.accepted().build();
    }
}
