package io.kk.bandhub.venueservice.utils;

import io.kk.bandhub.venueservice.exception.AccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class SecurityUtils {

    public static void verifyAuthorities(Authentication authentication, Integer userId) {
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
        Jwt jwt = jwtAuth.getToken();
        Long userIdFromToken = jwt.getClaim("userId");

        if (!userIdFromToken.equals(userId.longValue())) {
            throw new AccessException("You can only edit data that belongs to you.");
        }
    }

    public static void verifyAuthorities(Authentication authentication, Integer tenantId, Integer ownerId) {
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
        Jwt jwt = jwtAuth.getToken();
        Long userIdFromToken = jwt.getClaim("userId");

        if (!userIdFromToken.equals(tenantId.longValue()) && !userIdFromToken.equals(ownerId.longValue())) {
            throw new AccessException("You can only edit data that belongs to you.");
        }
    }
}
