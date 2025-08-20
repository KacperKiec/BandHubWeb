package io.kk.bandhub.userservice.service.impl;

import io.kk.bandhub.userservice.dto.AccountAuthDTO;
import io.kk.bandhub.userservice.dto.Mapper;
import io.kk.bandhub.userservice.exception.AccountError;
import io.kk.bandhub.userservice.exception.AccountException;
import io.kk.bandhub.userservice.model.AccountRole;
import io.kk.bandhub.userservice.repository.AccountRepository;
import io.kk.bandhub.userservice.service.AuthAccountService;
import io.kk.bandhub.userservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthAccountServiceImpl implements AuthAccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final Mapper mapper;

    @Value("${app.emergency-password}")
    private String emergencyPassword;

    public AccountAuthDTO findToAuth(String email) {
        var existingAccount = accountRepository.findAccountByEmail(email).orElseThrow(
                () -> new AccountException(AccountError.ACCOUNT_NOT_FOUND)
        );


        return AccountAuthDTO.builder()
                .id(existingAccount.getId())
                .email(existingAccount.getEmail())
                .password(existingAccount.getPassword())
                .roles(existingAccount.getAccountRoles().stream().map(AccountRole::getRole).map(mapper::toRoleDTO).collect(Collectors.toSet()))
                .build();
    }

    public Boolean validatePassword(Integer userId, String password) {
        var account = accountRepository.findAccountById(userId).orElseThrow(
                () -> new AccountException(AccountError.ACCOUNT_NOT_FOUND)
        );

        return passwordEncoder.matches(password, account.getPassword());
    }

    public void changePassword(Integer userId, String password) {
        var account = accountRepository.findAccountById(userId).orElseThrow(
                () -> new AccountException(AccountError.ACCOUNT_NOT_FOUND)
        );

        account.setPassword(passwordEncoder.encode(password));
        accountRepository.save(account);
    }

    public void remindPassword(String email) {
        var account = accountRepository.findAccountByEmail(email).orElseThrow(
                () -> new AccountException(AccountError.ACCOUNT_NOT_FOUND)
        );

        account.setPassword(passwordEncoder.encode(this.emergencyPassword));
        accountRepository.save(account);

        String subject = "Hasło tymczasowe";
        String emailContent = """
            Cześć %s,
        
            Otrzymaliśmy prośbę o zresetowanie hasła do Twojego konta w serwisie BandHubWeb.
            Twoje tymczasowe hasło to:
        
                %s
        
            Zaloguj się przy jego użyciu i jak najszybciej ustaw nowe, bezpieczne hasło.
            
            Pozdrawiamy,
            Zespół BandHubWeb
            """.formatted(account.getFirstName(), this.emergencyPassword);

        this.emailService.sendEmail(email, subject, emailContent);

    }
}
