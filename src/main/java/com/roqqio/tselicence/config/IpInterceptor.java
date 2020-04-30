package com.roqqio.tselicence.config;

import com.roqqio.tselicence.core.Interceptor;
import com.roqqio.tselicence.security.WithIpRestriction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class IpInterceptor extends Interceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(IpInterceptor.class.getName());
    private final String[] whitelist;

    @Autowired
    public IpInterceptor(Environment env) {
        String property = env.getProperty("custom.ipWhitelist");
        this.whitelist = property != null ? property.trim().split(",") : new String[]{};
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            if (method.getMethodAnnotation(WithIpRestriction.class) != null) {
                if (noMatch(request)) {
                    LOGGER.info(unauthorized(response, request));
                    return false;
                }
            }
        }
        return true;
    }

    private boolean noMatch(HttpServletRequest request) {
        if (whitelist.length == 0) {
            return false;
        }
        String remoteAddr = request.getRemoteAddr();
        for (String ip : whitelist) {
            if (remoteAddr.startsWith(ip)) {
                return false;
            }
        }
        return true;
    }
}
