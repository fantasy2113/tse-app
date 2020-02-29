package de.jos.tselicence.controller;

import de.jos.tselicence.core.interfaces.TaskRunner;
import de.jos.tselicence.core.interfaces.security.IAuthenticator;
import de.jos.tselicence.core.interfaces.security.IWhitelist;
import org.springframework.http.ResponseEntity;

import java.util.List;

abstract class Controller implements TaskRunner {
    private final IAuthenticator authenticator;
    private final IWhitelist whitelist;

    Controller(IAuthenticator authenticator, IWhitelist whitelist) {
        this.authenticator = authenticator;
        this.whitelist = whitelist;
    }

    IAuthenticator getAuthenticator() {
        return authenticator;
    }

    IWhitelist getWhitelist() {
        return whitelist;
    }

    <T> ResponseEntity<T> statusUnauthorized(T item) {
        return ResponseEntity.status(401).body(item);
    }

    <T> ResponseEntity<List<T>> statusUnauthorized(List<T> items) {
        return ResponseEntity.status(401).body(items);
    }

    <T> ResponseEntity<T> status404NoEntityFound(T item) {
        return ResponseEntity.status(404).body(item);
    }

    <T> ResponseEntity<T> statusOk(T item) {
        return ResponseEntity.ok().body(item);
    }

    <T> ResponseEntity<List<T>> statusOk(List<T> items) {
        return ResponseEntity.ok().body(items);
    }

    ResponseEntity<Void> statusUnauthorized() {
        return ResponseEntity.status(401).build();
    }

    ResponseEntity<Void> statusDeleteOk() {
        return ResponseEntity.status(204).build();
    }

    ResponseEntity<Void> statusOk() {
        return ResponseEntity.ok().build();
    }

    boolean isCrossInjection(final String data) {
        return data.contains("{") || data.contains("}") || data.contains("(") || data.contains(")")
                || data.contains("[") || data.contains("]") || data.contains("<") || data.contains(">")
                || data.contains("=") || data.contains("&") || data.contains("|") || data.contains(":")
                || data.contains(";") || data.contains("$") || data.contains("#") || data.contains("\"")
                || data.contains("'") || data.contains("+") || data.contains("?") || data.contains("%")
                || data.contains("/") || data.contains("\\");
    }
}
