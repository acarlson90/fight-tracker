package com.aaroncarlson.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Implementation for AuthenticationEntryPoint interface and provides the implementation for it's commence()
 * method. This method is called whenever an exception is thrown due to an unauthenticated User trying to
 * access a resource that requires authentication. In this example, simply respond with a 401 error
 * containing the exception message.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public void commence(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final AuthenticationException exception) throws ServletException, IOException {
        logger.error("Responding with unauthorized error. Message - {}", exception.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
    }

}
