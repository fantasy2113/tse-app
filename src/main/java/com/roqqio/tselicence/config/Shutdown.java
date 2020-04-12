package com.roqqio.tselicence.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Shutdown {
    @Bean
    public TerminateBean getTerminateBean() {
        return new TerminateBean();
    }
}
