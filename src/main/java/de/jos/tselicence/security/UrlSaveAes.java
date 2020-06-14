package de.jos.tselicence.security;

import de.jos.tselicence.core.Comp;
import de.jos.tselicence.core.interfaces.security.IEncryption;
import de.jos.tselicence.core.util.Toolbox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Optional;

@Component(Comp.URL_SAVE_AES)
public class UrlSaveAes implements IEncryption {
    private static final Logger LOGGER = LoggerFactory.getLogger(UrlSaveAes.class.getName());
    private final String secretKey;
    private final String salt;
    private final IvParameterSpec ivSpec;

    @Autowired
    public UrlSaveAes(Environment env) {
        this.salt = Toolbox.trim(env.getProperty("custom.aesSalt"));
        this.secretKey = Toolbox.trim(env.getProperty("custom.aesSecretKey"));
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        this.ivSpec = new IvParameterSpec(iv);
    }

    @Override
    public Optional<String> encrypt(String toEncrypt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            return Optional.of(URLEncoder.encode(Base64.getEncoder().encodeToString(cipher.doFinal(toEncrypt.getBytes(StandardCharsets.UTF_8))), "UTF-8"));
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> decrypt(String toDecrypt) {
        try {
            toDecrypt = URLDecoder.decode(toDecrypt, "UTF-8");
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            return Optional.of(URLEncoder.encode(new String(cipher.doFinal(Base64.getDecoder().decode(toDecrypt))), "UTF-8"));
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return Optional.empty();
    }
}
