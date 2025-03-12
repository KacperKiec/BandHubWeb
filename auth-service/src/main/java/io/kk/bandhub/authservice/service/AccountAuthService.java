package io.kk.bandhub.authservice.service;

import io.kk.bandhub.authservice.dto.AccountDTO;
import io.kk.bandhub.authservice.exception.AuthException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AccountAuthService implements UserDetailsService {

    public final RestTemplate restTemplate;
    private final String url = "http://localhost:9000/account/email?email=";

    public AccountAuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return restTemplate.getForObject(url + username, AccountDTO.class);
        } catch (Exception e) {
            throw new AuthException("User with given credentials could not be found");
        }
    }
}
