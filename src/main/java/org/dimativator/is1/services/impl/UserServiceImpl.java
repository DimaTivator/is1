package org.dimativator.is1.services.impl;

import org.dimativator.is1.dto.AuthDto;
import org.dimativator.is1.dto.RegisteredUserDto;
import org.dimativator.is1.dto.SignUpDto;
import org.dimativator.is1.jwt.UserAuthProvider;
import org.dimativator.is1.mappers.UserMapper;
import org.dimativator.is1.model.Role;
import org.dimativator.is1.model.User;
import org.dimativator.is1.repository.UserRepository;
import org.dimativator.is1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserAuthProvider userAuthProvider;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserAuthProvider userAuthProvider,
                           UserRepository userRepository) {
        this.userAuthProvider = userAuthProvider;
        this.userRepository = userRepository;
    }

    @Override
    public RegisteredUserDto register(SignUpDto signUpDto) {
        final User user = UserMapper.toEntity(signUpDto);
        user.setPassword(encodePassword(signUpDto.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        final RegisteredUserDto registeredUserDto = UserMapper.toRegisteredUserDto(user);
        registeredUserDto.setToken(userAuthProvider.createToken(user.getLogin(), user.getRole()));
        return registeredUserDto;
    }

    @Override
    public RegisteredUserDto authorize(AuthDto authDto) {
        final User user = userRepository.findByLogin(authDto.getLogin())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No such user"));
        final String requestPassword = encodePassword(authDto.getPassword());

        if (requestPassword.equals(user.getPassword())) {
            final RegisteredUserDto registeredUserDto = UserMapper.toRegisteredUserDto(user);
            registeredUserDto.setToken(userAuthProvider.createToken(user.getLogin(), user.getRole()));
            return registeredUserDto;
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
    }

    @Override
    public User getUserByToken(String token) {
        final String login = userAuthProvider.getLoginFromJwt(token);
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No such user"));
    }

    @Override
    public List<String> getAllUserNames() {
        return userRepository.findAll().stream().map(User::getLogin).toList();
    }

    @Override
    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElse(null);
    }

    @Override
    public boolean requestAdminRights(String token) {
        final User user = getUserByToken(token.split(" ")[1]);
        if (userRepository.existsByRole(Role.ADMIN) && user.getRole() != Role.ADMIN) {
            user.setRole(Role.POTENTIAL_ADMIN);
            userRepository.save(user);
            return false;
        }
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        return true;
    }

    private String encodePassword(String password) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-384");
            return new String(messageDigest.digest(password.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Couldn't encode password", e);
        }
    }
}
