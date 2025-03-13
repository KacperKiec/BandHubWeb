package io.kk.bandhub.authservice.service;

import io.kk.bandhub.authservice.dto.AccountDTO;
import io.kk.bandhub.authservice.exception.AuthException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AccountAuthService implements UserDetailsService {

    public final RestTemplate restTemplate;
    private final OAuth2Client oAuth2Client;
    private final String url;

    public AccountAuthService(RestTemplate restTemplate, OAuth2Client oAuth2Client, @Value("${app.services.account-url}") String url) {
        this.restTemplate = restTemplate;
        this.oAuth2Client = oAuth2Client;
        this.url = url;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String token = oAuth2Client.getAccessToken();
        //System.out.println(token);
        try {
            String completeUrl = url + username;
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<AccountDTO> response = restTemplate.exchange(
                    completeUrl,
                    HttpMethod.GET,
                    entity,
                    AccountDTO.class
            );
            return response.getBody();
        } catch (Exception e) {
            //System.out.println(e);
            throw new AuthException("User with given credentials could not be found");
        }
    }
}
