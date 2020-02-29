package de.jos.tselicence.controller;

import de.jos.tselicence.core.Comp;
import de.jos.tselicence.core.interfaces.security.IAuthenticator;
import de.jos.tselicence.core.interfaces.security.IWhitelist;
import de.jos.tselicence.core.util.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@RestController
public class ViewController {
    private final IAuthenticator authenticator;
    private final IWhitelist whitelist;

    @Autowired
    public ViewController(@Qualifier(Comp.AUTH) IAuthenticator authenticator, @Qualifier(Comp.WHITELIST) IWhitelist whitelist) {
        this.authenticator = authenticator;
        this.whitelist = whitelist;
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> view(HttpServletRequest req) {
        try {
            if (whitelist.noMatch(req.getLocalAddr())) {
                return ResponseEntity.status(401).body("");
            }
            Optional<String> optKey = parseKey(req.getCookies());
            if (optKey.isPresent() && authenticator.isOk(optKey.get())) {
                return ResponseEntity.ok().body(getIndexHtml());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(getLoginHtml());
    }

    private Optional<String> parseKey(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("ctoken")) {
                    return Optional.of(cookie.getValue());
                }
            }
        }
        return Optional.empty();
    }

    private String getIndexHtml() {
        try {
            return Resource.get("static/html/index.html");
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    private String getLoginHtml() {
        try {
            return Resource.get("static/html/login.html");
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}
