//package io.kk.bandhub.userservice.service;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jws;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import java.security.Key;
//import java.security.interfaces.RSAPrivateKey;
//import java.security.interfaces.RSAPublicKey;
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class JwtService {
//
//
//    public String generateToken(UserDetails userDetails) {
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .toList();
//        return Jwts.builder()
//                .setSubject(userDetails.getUsername())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
//                .claim("roles", roles)
//                .signWith(privateKey, SignatureAlgorithm.RS256)
//                .compact();
//    }
//
//    public String extractUsername(String token) {
//        return getClaims(token).getBody().getSubject();
//    }
//
//    public List<String> extractRoles(String token) {
//        return (List<String>) getClaims(token).getBody().get("roles");
//    }
//
//    public boolean isTokenValid(String token, String username) {
//        return extractUsername(token).equals(username) && !isTokenExpired(token);
//    }
//
//    private Jws<Claims> getClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(publicKey)
//                .build()
//                .parseClaimsJws(token);
//    }
//
//    private boolean isTokenExpired(String token) {
//        return getClaims(token).getBody().getExpiration().before(new Date());
//    }
//}
