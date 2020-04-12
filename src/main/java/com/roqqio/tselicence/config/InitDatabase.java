package com.roqqio.tselicence.config;

import com.roqqio.tselicence.core.DbSetup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class InitDatabase {
    @Autowired
    public InitDatabase(DbSetup dbSetup) {
        dbSetup.dbSetup();
    }
}
