package org.dimativator.is1.jwt;

import org.dimativator.is1.model.Role;
import org.springframework.security.core.Authentication;

public interface UserAuthProvider {
    String createToken(String login, Role role);

    String getLoginFromJwt(String token);

    Authentication validateToken(String token);
}
