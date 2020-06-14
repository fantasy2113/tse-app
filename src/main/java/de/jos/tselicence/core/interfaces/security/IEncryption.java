package de.jos.tselicence.core.interfaces.security;

import java.util.Optional;

public interface IEncryption {
    Optional<String> encrypt(String toEncrypt);

    Optional<String> decrypt(String toDecrypt);
}
