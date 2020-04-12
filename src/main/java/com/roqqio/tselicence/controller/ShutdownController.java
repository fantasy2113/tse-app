package com.roqqio.tselicence.controller;

import com.roqqio.tselicence.security.WithIpRestriction;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShutdownController implements ApplicationContextAware {

    private final String secret;
    private ApplicationContext context;

    @Autowired
    public ShutdownController(Environment env) {
        this.secret = env.getProperty("custom.shutdownPassword");
    }

    @WithIpRestriction
    @PostMapping("internal/shutdown")
    public void shutdown(@RequestParam String secret) {
        if (isSecret(secret)) {
            ((ConfigurableApplicationContext) context).close();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.context = ctx;
    }

    private boolean isSecret(@RequestParam String secret) {
        return this.secret != null && this.secret.equals(secret);
    }
}
