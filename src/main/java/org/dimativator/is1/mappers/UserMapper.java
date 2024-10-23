package org.dimativator.is1.mappers;

import org.dimativator.is1.dto.RegisteredUserDto;
import org.dimativator.is1.dto.SignUpDto;
import org.dimativator.is1.dto.UserDto;
import org.dimativator.is1.model.User;

public class UserMapper {
    public static User toEntity(SignUpDto signUpDto) {
        return User.builder()
                .login(signUpDto.getLogin())
//                .password(signUpDto.getPassword())
                .role(signUpDto.getRole())
                .build();
    }

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .build();
    }

    public static RegisteredUserDto toRegisteredUserDto(User user) {
        // token is set in UserServiceImpl
        return RegisteredUserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .role(user.getRole())
                .build();
    }
}
