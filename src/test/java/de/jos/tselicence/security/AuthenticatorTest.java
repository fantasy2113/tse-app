package de.jos.tselicence.security;

import de.jos.tselicence.config.Context;
import de.jos.tselicence.core.entities.User;
import de.jos.tselicence.core.interfaces.repositories.IUserRepository;
import de.jos.tselicence.core.interfaces.security.IAuthenticator;
import de.jos.tselicence.core.util.DbUtils;
import de.jos.tselicence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import testutil.DbUtilsTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AuthenticatorTest {
    IAuthenticator authenticator;
    IUserRepository userRepository;
    User user;

    @BeforeEach
    void setUp() {
        DbUtilsTest.drop();
        DbUtilsTest.create();

        authenticator = Context.getBean(Authenticator.class);
        userRepository = Context.getBean(UserRepository.class);
        DbUtils.insertAdmin(userRepository);
        user = DbUtilsTest.getValidUser(userRepository);
    }

    @Test
    void isFailWithOneKeyShouldReturnTrue() {
        String key = "";

        boolean result = authenticator.isFail(key);

        assertTrue(result);
    }

    @Test
    void isFailWithTwoKeysShouldReturnTrue() {
        String key1 = "dasdsa";
        String key2 = "dasdasds";

        boolean result = authenticator.isFail(key1, key2);

        assertTrue(result);
    }

    @Test
    void isFailWithNullShouldReturnTrue() {
        boolean result = authenticator.isFail(null);

        assertTrue(result);
    }

    @Test
    void isFailWithNullKeyShouldReturnTrue() {
        String key = null;

        boolean result = authenticator.isFail(key);

        assertTrue(result);
    }

    @Test
    void isFailWithTwoNullKeysShouldReturnTrue() {
        String key1 = null;
        String key2 = null;

        boolean result = authenticator.isFail(key1, key2);

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

    @Test
    void getTokenWithWithRightKeyAndWrongKeysShouldReturnFalse() {
        String rightKey = authenticator.getToken(user.getUsername(), user.getPassword()).get();
        String wrongKey = "fsdfsdfsdfds";

        boolean result = authenticator.isFail(rightKey, wrongKey);

        assertFalse(result);
    }
}