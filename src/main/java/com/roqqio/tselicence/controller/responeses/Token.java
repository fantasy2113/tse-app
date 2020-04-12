package com.roqqio.tselicence.controller.responeses;

public class Token {

    private final String token;

    public Token(final String token) {
        this.token = token;
    }

    public Token() {
        this.token = "";
    }

    public String getToken() {
        return token;
    }
}
