package io.kk.bandhub.authservice.controller;

import io.kk.bandhub.authservice.dto.AccountDTO;
import io.kk.bandhub.authservice.dto.AuthRequest;
import io.kk.bandhub.authservice.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    ResponseEntity<Void> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        AccountDTO user = (AccountDTO) authentication.getPrincipal();
        String token = jwtService.generateToken(user, user.getId());

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(3600)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<Integer> getCurrentUser(Authentication authentication) {
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        Long userId = jwt.getClaim("userId");
        return ResponseEntity.ok(userId.intValue());
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(true);
    }

}
