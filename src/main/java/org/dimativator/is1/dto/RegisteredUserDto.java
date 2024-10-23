package org.dimativator.is1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dimativator.is1.model.Role;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredUserDto {
    private Long id;
    private String login;
    private String token;
    private Role role;
}
