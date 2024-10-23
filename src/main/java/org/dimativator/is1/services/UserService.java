package org.dimativator.is1.services;

import org.dimativator.is1.dto.AuthDto;
import org.dimativator.is1.dto.RegisteredUserDto;
import org.dimativator.is1.dto.SignUpDto;
import org.dimativator.is1.model.User;

import java.util.List;

public interface UserService {
    RegisteredUserDto register(SignUpDto signUpDto);

    RegisteredUserDto authorize(AuthDto authDto);

    boolean requestAdminRights(String token);

    User getUserByToken(String token);

    User getUserByLogin(String login);

    List<String> getAllUserNames();
}
