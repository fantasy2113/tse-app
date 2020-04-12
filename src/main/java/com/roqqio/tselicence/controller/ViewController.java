package com.roqqio.tselicence.controller;

import com.roqqio.tselicence.core.Comp;
import com.roqqio.tselicence.core.interfaces.security.IAuthenticator;
import com.roqqio.tselicence.core.util.Resource;
import com.roqqio.tselicence.security.WithIpRestriction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class ViewController extends Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(ViewController.class.getName());
    private final IAuthenticator authenticator;

    @Autowired
    public ViewController(@Qualifier(Comp.AUTH) IAuthenticator authenticator) {
        this.authenticator = authenticator;
    }

    @WithIpRestriction
    @GetMapping(value = "/app", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> view(HttpServletRequest req) {
        try {
            if (authenticator.isOk(req)) {
                return ResponseEntity.ok().body(getIndexHtml());
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return ResponseEntity.ok().body(getLoginHtml());
    }

    private String getIndexHtml() {
        try {
            return Resource.get("html/index.html");
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    private String getLoginHtml() {
        try {
            return Resource.get("html/login.html");
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}
