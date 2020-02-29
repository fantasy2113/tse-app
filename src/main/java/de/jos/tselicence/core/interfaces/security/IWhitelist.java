package de.jos.tselicence.core.interfaces.security;

public interface IWhitelist {
    boolean noMatch(String localAddr);

    boolean anyMatch(String localAddr);
}
