package de.jos.tselicence.security;

import de.jos.tselicence.config.Context;
import de.jos.tselicence.core.Comp;
import de.jos.tselicence.core.interfaces.security.IWhitelist;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component(Comp.WHITELIST)
public class Whitelist implements IWhitelist {
    private List<String> whitelist;

    public Whitelist() {
        this.whitelist = new ArrayList<>(Arrays.asList(Objects.requireNonNull(Context.getBean(Environment.class)
                .getProperty("custom.ipWhitelist")).split(",")));
    }

    @Override
    public boolean noMatch(String localAddr) {
        if (localAddr == null) {
            return true;
        }
        for (String ip : whitelist) {
            if (localAddr.startsWith(ip)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean anyMatch(String localAddr) {
        return !noMatch(localAddr);
    }
}
