package com.roqqio.tselicence.config;

import com.roqqio.tselicence.core.util.Toolbox;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "custom")
public class Properties {
    private String ipWhitelist;
    private String aesSecretKey;
    private String aesSalt;
    private String username;
    private String password;
    private String shutdownPassword;
    private String logDir;

    public String getIpWhitelist() {
        return ipWhitelist;
    }

    public void setIpWhitelist(String ipWhitelist) {
        this.ipWhitelist = Toolbox.trim(ipWhitelist);
    }

    public String getAesSecretKey() {
        return aesSecretKey;
    }

    public void setAesSecretKey(String aesSecretKey) {
        this.aesSecretKey = Toolbox.trim(aesSecretKey);
    }

    public String getAesSalt() {
        return aesSalt;
    }

    public void setAesSalt(String aesSalt) {
        this.aesSalt = Toolbox.trim(aesSalt);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = Toolbox.trim(password);
    }

    public String getShutdownPassword() {
        return shutdownPassword;
    }

    public void setShutdownPassword(String shutdownPassword) {
        this.shutdownPassword = Toolbox.trim(shutdownPassword);
    }

    public String getLogDir() {
        return logDir;
    }

    public void setLogDir(String logDir) {
        this.logDir = logDir;
    }
}

