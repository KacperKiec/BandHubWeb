package io.kk.bandhub.imageservice.config.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class OAuth2Client {
    private final String url;
    private final String clientId;
    private final String clientSecret;
    private final String scope;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    private String accessToken;
    private long tokenExpirationTime;
    private final ReentrantLock lock = new ReentrantLock();

    public OAuth2Client(@Value("${app.services.token-url}") String url,
                        @Value("${app.oauth2.client-id}") String clientId,
                        @Value("${app.oauth2.client-secret}") String clientSecret,
                        @Value("${app.oauth2.scope}") String scope) {
        this.url = url;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.scope = scope;
    }

    public String getAccessToken() {
        lock.lock();
        //System.out.println(System.currentTimeMillis() - tokenExpirationTime);
        try {
            if (accessToken == null || System.currentTimeMillis() >= tokenExpirationTime) {
                refreshAccessToken();
            }
            return accessToken;
        } finally {
            lock.unlock();
        }
    }

    private void refreshAccessToken() {

        String grantType = "client_credentials";

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

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        //System.out.println(response.getBody());

        try {
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            this.accessToken = jsonResponse.get("access_token").asText();
            int expiresIn = jsonResponse.get("expires_in").asInt();
            //System.out.println(expiresIn);
            this.tokenExpirationTime = System.currentTimeMillis() + (expiresIn * 1000L) - 5000;
            //System.out.println(System.currentTimeMillis());
            // System.out.println(this.tokenExpirationTime);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse OAuth2 token response", e);
        }
    }
}
