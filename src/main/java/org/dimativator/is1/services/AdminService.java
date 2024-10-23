package org.dimativator.is1.services;

import org.dimativator.is1.dto.UserDto;
import org.dimativator.is1.model.Role;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService {
    List<UserDto> getAllPotentialAdmins(Pageable paging);

    void setNewRoleToPotentialAdmin(Long id, Role role);
}
