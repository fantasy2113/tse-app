package com.roqqio.tselicence.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class Interceptor {
    protected String unauthorized(HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.setStatus(401);
        String unauthorizedMessage = getUnauthorizedMessage(request);
        response.getWriter().write(unauthorizedMessage);
        return unauthorizedMessage;
    }

    private String getUnauthorizedMessage(HttpServletRequest request) {
        String clientInfo = request.getRemoteHost() + " Host (" + request.getRemoteAddr() + " Addr : " + request.getRemotePort() + " Port)";
        return "Unauthorized access from remote: " + clientInfo;
    }
}
