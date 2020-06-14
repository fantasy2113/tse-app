package de.jos.tselicence.core.util;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Resource {

    private Resource() {
    }

    public static String get(String resource) throws IOException {
        return IOUtils.toString(new ClassPathResource(resource).getInputStream(), StandardCharsets.UTF_8);
    }
}
