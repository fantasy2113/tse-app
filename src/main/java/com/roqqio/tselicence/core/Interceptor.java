package com.roqqio.tselicence.core;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class Interceptor {
    protected void setUnauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(401);
        response.getWriter().write("unauthorized");
    }
}
