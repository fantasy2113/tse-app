package com.roqqio.tselicence.security;

import com.roqqio.tselicence.core.DbSetup;
import com.roqqio.tselicence.core.entities.User;
import com.roqqio.tselicence.core.interfaces.repositories.IUserRepository;
import com.roqqio.tselicence.core.interfaces.security.IAuthenticator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import testutil.DbUtilsTest;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticatorTest {
    @Autowired
    IAuthenticator authenticator;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    DbSetup dbSetup;
    @Autowired
    Environment env;
    User user;

    @BeforeEach
    void setUp() {
        DbUtilsTest.drop(dbSetup);
        DbUtilsTest.create(dbSetup);

        DbUtilsTest.insertDefaultUser(env, userRepository);
        user = DbUtilsTest.getValidUser(userRepository);
    }

    @Test
    void isFailWithOneKeyShouldReturnTrue() {
        String key = "";

        boolean result = authenticator.isFail(key);

        assertTrue(result);
    }


    //@Test
    void isFailWithNullShouldReturnTrue() {
        HttpServletRequest req = null;
        boolean result = authenticator.isFail(req);

        assertTrue(result);
    }

    @Test
    void isFailWithNullKeyShouldReturnTrue() {
        String key = null;

        boolean result = authenticator.isFail(key);

        assertTrue(result);
    }

    @Test
    void getTokenWithLoginAndPasswordShouldReturnEmpty() {
        Optional<String> result = authenticator.getToken(null, null);

        assertFalse(result.isPresent());
    }

    @Test
    void getTokenWithLoginShouldReturnEmpty() {
        Optional<String> result = authenticator.getToken(null, "");

        assertFalse(result.isPresent());
    }

    @Test
    void getTokenWithPasswordShouldReturnEmpty() {
        Optional<String> result = authenticator.getToken("", null);

        assertFalse(result.isPresent());
    }

    @Test
    void getTokenWithWrongPasswordAndShouldReturnEmpty() {
        Optional<String> result = authenticator.getToken("-", "-");

        assertFalse(result.isPresent());
    }

    @Test
    void getTokenWithRightPasswordAndLoginShouldReturnKey() {
        Optional<String> result = authenticator.getToken(user.getUsername(), user.getPassword());

        assertTrue(result.isPresent());
    }

    @Test
    void isFailWithRightKeyShouldReturnFalse() {
        String rightKey = authenticator.getToken(user.getUsername(), user.getPassword()).get();

        boolean result = authenticator.isFail(rightKey);

        assertFalse(result);
    }

    @Test
    void isOkWithRightKeyShouldReturnTrue() {
        String rightKey = authenticator.getToken(user.getUsername(), user.getPassword()).get();

        boolean result = authenticator.isOk(rightKey);

        assertTrue(result);
    }

    @Test
    void isOkWithWrongKeyShouldReturnFalse() {
        boolean result = authenticator.isOk("wrongKey");

        assertFalse(result);
    }
}