package com.roqqio.tselicence.security;

import com.roqqio.tselicence.core.Comp;
import com.roqqio.tselicence.core.entities.User;
import com.roqqio.tselicence.core.interfaces.TaskRunner;
import com.roqqio.tselicence.core.interfaces.repositories.IUserRepository;
import com.roqqio.tselicence.core.interfaces.security.IAuthenticator;
import com.roqqio.tselicence.core.interfaces.security.ICrypt;
import com.roqqio.tselicence.core.interfaces.security.IJwtToken;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component(Comp.AUTH)
public final class Authenticator implements IAuthenticator, TaskRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Authenticator.class.getName());
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
    public boolean isFail(String token) {
        if (token == null || token.isEmpty()) {
            return true;
        }
        try {
            return !isDecode(token);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return true;
        }
    }

    @Override
    public boolean isOk(String token) {
        return !isFail(token);
    }

    @Override
    public boolean isOk(HttpServletRequest req) {
        return isOk(parseToken(req));
    }

    @Override
    public boolean isFail(HttpServletRequest req) {
        return !isOk(req);
    }

    @Override
    public Optional<String> getToken(String username, String plainPassword) {
        if (isCheck(username, plainPassword)) {
            username = username.trim();
            plainPassword = plainPassword.trim();
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

    private String parseToken(HttpServletRequest req) {
        String hToken = req.getHeader("htoken");
        if (hToken != null) {
            return hToken;
        }
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return "";
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("ctoken")) {
                return cookie.getValue();
            }
        }
        return "";
    }

    private boolean isCheck(final String username, final String plainPassword) {
        return username != null && plainPassword != null;
    }
}
