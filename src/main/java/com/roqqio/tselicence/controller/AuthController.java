package com.roqqio.tselicence.controller;

import com.roqqio.tselicence.controller.responeses.Token;
import com.roqqio.tselicence.core.Comp;
import com.roqqio.tselicence.core.interfaces.security.IAuthenticator;
import com.roqqio.tselicence.security.WithAuthentication;
import com.roqqio.tselicence.security.WithIpRestriction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public final class AuthController extends Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class.getName());
    private final IAuthenticator authenticator;

    @Autowired
    public AuthController(@Qualifier(Comp.AUTH) IAuthenticator authenticator) {
        this.authenticator = authenticator;
    }

    @WithIpRestriction
    @GetMapping("/external/auth")
    public ResponseEntity<Token> auth(@RequestHeader("username") String username, @RequestHeader("password") String password) {
        LOGGER.info(username);
        Optional<String> token = authenticator.getToken(username, password);
        return token.isPresent() ? statusOk(new Token(token.get())) : statusUnauthorized(new Token());
    }

    @WithIpRestriction
    @WithAuthentication
    @PostMapping("/external/auth-check")
    public ResponseEntity<Void> authCheck() {
        return statusOk();
    }
}
