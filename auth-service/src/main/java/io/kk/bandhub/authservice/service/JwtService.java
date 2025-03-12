package io.kk.bandhub.authservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private final RSAPrivateKey privateKey;

    public JwtService(RSAPrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public String generateToken(UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .claim("roles", roles)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }
}