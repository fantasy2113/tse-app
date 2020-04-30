package com.roqqio.tselicence.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
class UrlAesTest {
    @Autowired
    UrlSaveAes aes;

    @Test
    void encrypt1() {
        String originalString = "howtodoinjava.com";
        Optional<String> encrypt = aes.encrypt(originalString);
        System.out.println(encrypt.get());
        assertTrue(!encrypt.equals(originalString));
    }

    @Test
    void encrypt999999() {
        String originalString = "999999";
        Optional<String> encrypt = aes.encrypt(originalString);
        System.out.println(encrypt.get());
        assertTrue(!encrypt.equals(originalString));
    }

    @Test
    void decrypt() {
        String originalString = "howtodoinjava.com";
        Optional<String> encrypt = aes.encrypt(originalString);
        System.out.println(encrypt.get());
        assertTrue(!encrypt.equals(originalString));

        Optional<String> decrypt = aes.decrypt(encrypt.get());

        assertEquals(originalString, decrypt.get());
    }

    @Test
    void decrypt99999() {
        String originalString = "999999";
        Optional<String> encrypt = aes.encrypt(originalString);
        System.out.println(encrypt.get());
        assertTrue(!encrypt.equals(originalString));

        Optional<String> decrypt = aes.decrypt(encrypt.get());

        assertEquals(originalString, decrypt.get());
    }
}