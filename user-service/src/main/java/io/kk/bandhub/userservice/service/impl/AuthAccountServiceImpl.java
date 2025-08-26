package io.kk.bandhub.userservice.service.impl;

import io.kk.bandhub.userservice.dto.AccountAuthDTO;
import io.kk.bandhub.userservice.dto.Mapper;
import io.kk.bandhub.userservice.exception.AccountError;
import io.kk.bandhub.userservice.exception.AccountException;
import io.kk.bandhub.userservice.model.AccountRole;
import io.kk.bandhub.userservice.repository.AccountRepository;
import io.kk.bandhub.userservice.service.AuthAccountService;
import io.kk.bandhub.userservice.service.EmailService;
import io.kk.bandhub.userservice.utils.PasswordGenerator;
import lombok.RequiredArgsConstructor;
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

        var password = PasswordGenerator.generateRandomString(12);

        account.setPassword(passwordEncoder.encode(password));
        accountRepository.save(account);

        String subject = "Hasło tymczasowe";
        String emailContent = """
        <!DOCTYPE html>
        <html lang="pl">
        <head>
            <meta charset="UTF-8">
            <title>Reset Hasła</title>
            <style>
                body { font-family: Arial, sans-serif; background-color: #f4f4f7; margin: 0; padding: 0; }
                .container { max-width: 600px; margin: 40px auto; background: #fff; padding: 30px; border-radius: 10px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); }
                h2 { color: #333; }
                .password { background: #f0f0f0; padding: 8px 12px; border-radius: 5px; font-family: monospace; font-weight: bold; }
            </style>
        </head>
        <body>
            <div class="container">
                <h2>Hasło tymczasowe</h2>
                <p>Cześć <strong>%s</strong>,</p>
                <p>Otrzymaliśmy prośbę o zresetowanie hasła do Twojego konta w serwisie BandHubWeb.<br>
                   Twoje tymczasowe hasło to:</p>
                <p class="password">%s</p>
                <p>Zaloguj się przy jego użyciu i jak najszybciej ustaw nowe, bezpieczne hasło.</p>
                <p>Pozdrawiamy,<br>Zespół BandHubWeb</p>
            </div>
        </body>
        </html>
        """.formatted(account.getFirstName(), password);

        this.emailService.sendEmail(email, subject, emailContent);

    }
}
