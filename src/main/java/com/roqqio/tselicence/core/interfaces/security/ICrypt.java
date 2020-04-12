package com.roqqio.tselicence.core.interfaces.security;

public interface ICrypt {
    String hashPassword(String plainTextPassword);

    boolean isPassword(final String plainPassword, final String hashedPassword);
}
