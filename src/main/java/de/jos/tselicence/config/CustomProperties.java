package de.jos.tselicence.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "custom")
public class CustomProperties {
    private String privateKey;
    private String publicKey;
    private String extPublicKey;
    private String ipWhiteList;

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getIpWhiteList() {
        return ipWhiteList;
    }

    public void setIpWhiteList(String ipWhiteList) {
        this.ipWhiteList = ipWhiteList;
    }

    public String getExtPublicKey() {
        return extPublicKey;
    }

    public void setExtPublicKey(String extPublicKey) {
        this.extPublicKey = extPublicKey;
    }
}

