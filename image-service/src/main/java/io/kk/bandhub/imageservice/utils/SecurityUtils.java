package io.kk.bandhub.imageservice.utils;

import io.kk.bandhub.imageservice.exception.AccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class SecurityUtils {

    public static void verifyAuthorities(Authentication authentication, Integer userId) {

        var uid = getUserIdFromAuthentication(authentication);

        if (!uid.equals(userId.longValue())) {
            throw new AccessException("You can only edit data that belongs to you.");
        }
    }

    public static Long getUserIdFromAuthentication(Authentication authentication) {
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
        Jwt jwt = jwtAuth.getToken();
        return jwt.getClaim("userId");
    }
}
