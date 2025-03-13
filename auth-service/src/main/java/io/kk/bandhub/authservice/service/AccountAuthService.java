package io.kk.bandhub.authservice.service;

import io.kk.bandhub.authservice.dto.AccountDTO;
import io.kk.bandhub.authservice.exception.AuthException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AccountAuthService implements UserDetailsService {

    public final RestTemplate restTemplate;
    private final String url;

    public AccountAuthService(RestTemplate restTemplate, @Value("${app.services.account-url}") String url) {
        this.restTemplate = restTemplate;
        this.url = url;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            String completeUrl = url + username;
            return restTemplate.getForObject(completeUrl, AccountDTO.class);
        } catch (Exception e) {
            throw new AuthException("User with given credentials could not be found");
        }
    }
}
