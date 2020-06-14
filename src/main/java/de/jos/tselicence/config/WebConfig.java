package de.jos.tselicence.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final IpInterceptor ipInterceptor;
    private final AuthInterceptor authenticationInterceptor;

    @Autowired
    public WebConfig(IpInterceptor ipInterceptor, AuthInterceptor authenticationInterceptor) {
        this.ipInterceptor = ipInterceptor;
        this.authenticationInterceptor = authenticationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipInterceptor);
        registry.addInterceptor(authenticationInterceptor);
    }
}
