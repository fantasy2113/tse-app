package de.jos.tselicence.core.interfaces.security;

import java.util.Optional;

public interface IAuthenticator {
    boolean isFail(String... tokens);

    boolean isOk(String... tokens);

    Optional<String> getToken(String username, String plainPassword);
}
