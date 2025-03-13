package io.kk.bandhub.authservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class OAuth2Client {
    private final String url = "http://localhost:8020/oauth2/token";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getAccessToken() {
        RestTemplate restTemplate = new RestTemplate();

        String clientId = "oidc-client";
        String clientSecret = "secret";
        String grantType = "client_credentials";
        String scope = "read";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", grantType);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("scope", scope);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        headers.add("Authorization", "Basic " + encodedCredentials);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        try {
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            return jsonResponse.get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse OAuth2 token response", e);
        }
    }
}
