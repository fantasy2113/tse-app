package de.jos.tselicence.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;

public class TerminateBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(TerminateBean.class.getName());

    @PreDestroy
    public void onDestroy() throws Exception {
        LOGGER.info("Spring Container is destroyed!");
    }
}
