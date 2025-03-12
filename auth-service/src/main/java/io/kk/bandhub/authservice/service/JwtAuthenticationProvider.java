package io.kk.bandhub.authservice.service;

import io.kk.bandhub.authservice.exception.AuthException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final AccountAuthService accountAuthService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public JwtAuthenticationProvider(AccountAuthService accountAuthService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.accountAuthService = accountAuthService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = accountAuthService.loadUserByUsername(email);

        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new AuthException("Authentication with given credentials failure");
        }

        var token = jwtService.generateToken(userDetails);

        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
