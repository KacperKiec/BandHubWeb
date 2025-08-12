package io.kk.bandhub.chatservice.config.custom;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * This class handles authentication failures in the application.
 * It implements the {@link AuthenticationEntryPoint} interface and is used
 * to return an appropriate HTTP response when a user tries to access a secured
 * resource without proper authentication.
 *
 * <p>The handler sets the response status to 401 (Unauthorized) and returns
 * a JSON response with an error code and message.</p>
 */
@Component
public class AuthenticationFailureHandler implements AuthenticationEntryPoint {

    /**
     * Handles the commencement of an authentication failure.
     * This method is triggered when an unauthenticated user attempts to access
     * a protected resource.
     *
     * @param request the {@link HttpServletRequest} that resulted in an authentication failure
     * @param response the {@link HttpServletResponse} to send the error response
     * @param authException the exception that triggered this authentication failure
     */
    @SneakyThrows
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"code\": \"401\", \"message\": \"Access denied. Please log in.\"}");
    }
}
