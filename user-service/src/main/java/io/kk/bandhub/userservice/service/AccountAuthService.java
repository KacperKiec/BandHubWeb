package io.kk.bandhub.userservice.service;

import io.kk.bandhub.userservice.exception.AccountError;
import io.kk.bandhub.userservice.exception.AccountException;
import io.kk.bandhub.userservice.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountAuthService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public AccountAuthService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return accountRepository.findAccountByEmail(username).orElseThrow(
                () -> new AccountException(AccountError.ACCOUNT_NOT_FOUND)
        );
    }
}
