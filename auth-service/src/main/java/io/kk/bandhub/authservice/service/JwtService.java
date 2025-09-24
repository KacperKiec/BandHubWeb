package io.kk.bandhub.authservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private final RSAPrivateKey privateKey;
    private final String port;

    @Value("${app.address}")
    private String appAddress;

    public JwtService(RSAPrivateKey privateKey, @Value("${server.port}") String port) {
        this.privateKey = privateKey;
        this.port = port;
    }

    public String generateToken(UserDetails userDetails, Integer userId) {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String appIp;
        try {
            appIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        String issuer = appIp + ":" + port;

        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 60);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .claim("roles", roles)
                .claim("userId", userId)
                .setIssuer(appAddress)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }
}