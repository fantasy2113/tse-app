package com.roqqio.tselicence.security;

import com.roqqio.tselicence.core.Comp;
import com.roqqio.tselicence.core.interfaces.security.ICrypt;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component(Comp.CRYPT)
public class Crypt implements ICrypt {

    @Override
    public String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    @Override
    public boolean isPassword(final String plainPassword, final String hashedPassword) {
        if (plainPassword == null || hashedPassword == null || hashedPassword.length() != 60) {
            return false;
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
