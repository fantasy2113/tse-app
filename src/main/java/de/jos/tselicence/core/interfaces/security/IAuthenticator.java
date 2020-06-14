package de.jos.tselicence.core.interfaces.security;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface IAuthenticator {
    boolean isOk(String token);

    boolean isOk(HttpServletRequest req);

    boolean isFail(String token);

    boolean isFail(HttpServletRequest req);

    Optional<String> getToken(String username, String plainPassword);
}
