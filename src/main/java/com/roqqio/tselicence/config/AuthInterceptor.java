package com.roqqio.tselicence.config;

import com.roqqio.tselicence.core.Comp;
import com.roqqio.tselicence.core.Interceptor;
import com.roqqio.tselicence.core.interfaces.security.IAuthenticator;
import com.roqqio.tselicence.security.WithAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor extends Interceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthInterceptor.class.getName());

    private final IAuthenticator authenticator;

    @Autowired
    public AuthInterceptor(@Qualifier(Comp.AUTH) IAuthenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            if (method.getMethodAnnotation(WithAuthentication.class) != null) {
                if (authenticator.isFail(request)) {
                    LOGGER.info(unauthorized(response, request));
                    return false;
                }
            }
        }
        return true;
    }
}
