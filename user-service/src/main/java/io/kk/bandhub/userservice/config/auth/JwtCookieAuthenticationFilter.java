package io.kk.bandhub.userservice.config.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JwtCookieAuthenticationFilter extends OncePerRequestFilter {

    private static final String JWT_COOKIE_NAME = "jwt";
    private final JwtDecoder jwtDecoder;

    public JwtCookieAuthenticationFilter(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            Optional<String> token = extractJwtFromCookie(request);
            if (token.isPresent()) {
                //System.out.println("JWT token found, attempting authentication");
                JwtAuthenticationToken authentication = authenticate(token.get());
                //System.out.println("Authentication successful for user: " +
                //        authentication.getName());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                //System.out.println("No JWT token found in cookies");
            }
        } catch (JwtException e) {
            //System.out.println("JWT validation failed: " + e.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            //System.out.println("No cookies found in request");
            return Optional.empty();
        }

//        System.out.println("Received cookies:");
//        Arrays.stream(request.getCookies())
//                .forEach(cookie -> System.out.println(
//                        "Cookie: " + cookie.getName() +
//                                " = " + cookie.getValue() +
//                                " | Domain: " + cookie.getDomain() +
//                                " | Path: " + cookie.getPath() +
//                                " | Secure: " + cookie.getSecure()));

        return Arrays.stream(request.getCookies())
                .filter(cookie -> JWT_COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    private JwtAuthenticationToken authenticate(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        List<String> roles = jwt.getClaimAsStringList("roles");

        if (roles == null) {
            roles = Collections.emptyList();
        }

        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        //System.out.println("Mapped authorities: " + authorities);

        return new JwtAuthenticationToken(jwt, authorities);
    }
}
