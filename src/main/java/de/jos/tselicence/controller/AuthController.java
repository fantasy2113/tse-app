package de.jos.tselicence.controller;

import de.jos.tselicence.controller.responeses.Token;
import de.jos.tselicence.core.Comp;
import de.jos.tselicence.core.interfaces.security.IAuthenticator;
import de.jos.tselicence.core.interfaces.security.IWhitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public final class AuthController extends Controller {
    @Autowired
    public AuthController(@Qualifier(Comp.AUTH) IAuthenticator authenticator, @Qualifier(Comp.WHITELIST) IWhitelist whitelist) {
        super(authenticator, whitelist);
    }

    @GetMapping("/auth")
    public ResponseEntity<Token> auth(@RequestHeader("username") String username, @RequestHeader("password") String password) {
        Optional<String> token = getAuthenticator().getToken(username, password);
        return token.isPresent() ? statusOk(new Token(token.get())) : statusUnauthorized(new Token());
    }
}
