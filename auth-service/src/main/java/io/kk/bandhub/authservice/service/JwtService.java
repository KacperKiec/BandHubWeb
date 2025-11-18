package io.kk.bandhub.authservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final RSAPrivateKey privateKey;

    @Value("${app.oauth2.token-expiration-time}")
    private Integer expirationTime;

    @Value("${app.address}")
    private String appAddress;

    public String generateToken(UserDetails userDetails, Integer userId) {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime * 1000))
                .claim("roles", roles)
                .claim("userId", userId)
                .setIssuer(appAddress)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }
}