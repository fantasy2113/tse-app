package de.jos.tselicence.security;

import de.jos.tselicence.core.Comp;
import de.jos.tselicence.core.entities.User;
import de.jos.tselicence.core.interfaces.TaskRunner;
import de.jos.tselicence.core.interfaces.repositories.IUserRepository;
import de.jos.tselicence.core.interfaces.security.IAuthenticator;
import de.jos.tselicence.core.interfaces.security.ICrypt;
import de.jos.tselicence.core.interfaces.security.IJwtToken;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component(Comp.AUTH)
public final class Authenticator implements IAuthenticator, TaskRunner {
    private final Long TTL_MILLIS = TimeUnit.DAYS.toMillis(3);
    private final IJwtToken jwtToken;
    private final IUserRepository userRepository;
    private final ICrypt crypt;

    @Autowired
    public Authenticator(@Qualifier(Comp.JWT) IJwtToken jwtToken,
                         @Qualifier(Comp.USER_REP) IUserRepository userRepository,
                         @Qualifier(Comp.CRYPT) ICrypt crypt) {
        this.jwtToken = jwtToken;
        this.userRepository = userRepository;
        this.crypt = crypt;
    }

    @Override
    public boolean isFail(String... tokens) {
        if (tokens == null) {
            return true;
        }
        for (String key : tokens) {
            if (key == null || key.isEmpty()) {
                continue;
            }
            try {
                if (isDecode(key)) {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public boolean isOk(String... tokens) {
        return !isFail(tokens);
    }

    @Override
    public Optional<String> getToken(final String username, final String plainPassword) {
        if (isCheck(username, plainPassword)) {
            Optional<User> optionalUser = userRepository.get(username);
            if (optionalUser.isPresent() && optionalUser.get().isActive()) {
                User user = optionalUser.get();
                if (crypt.isPassword(plainPassword, user.getPassword())) {
                    return Optional.of(jwtToken.create(String.valueOf(user.getId()), user.getUsername(), user.getUserRole(), TTL_MILLIS));
                }
            }
        }
        return Optional.empty();
    }

    private boolean isDecode(String token) {
        Claims decodedToken = jwtToken.decode(token);
        Optional<User> optionalUser = userRepository.get(decodedToken.getIssuer());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            run(() -> {
                userRepository.updateLastLogin(user);
            });
            return decodedToken.getId().equals(String.valueOf(user.getId()))
                    && decodedToken.getIssuer().equals(user.getUsername());
        }
        return false;
    }

    private boolean isCheck(final String username, final String plainPassword) {
        return username != null && plainPassword != null;
    }
}
