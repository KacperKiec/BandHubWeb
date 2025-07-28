package io.kk.bandhub.userservice.config.init;

import io.kk.bandhub.userservice.exception.RoleError;
import io.kk.bandhub.userservice.exception.RoleException;
import io.kk.bandhub.userservice.model.Account;
import io.kk.bandhub.userservice.model.AccountRole;
import io.kk.bandhub.userservice.repository.AccountRepository;
import io.kk.bandhub.userservice.repository.JpaAccountRepository;
import io.kk.bandhub.userservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final JpaAccountRepository jpaAccountRepository;
    private final RoleInitializer roleInitializer;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {
        roleInitializer.initialize();
        if (jpaAccountRepository.count() == 0) {
            Account account = new Account();
            account.setEmail("admin@example.com");
            account.setPassword(passwordEncoder.encode("admin"));
            account.setFirstName("Admin");
            account.setLastName("Admin");
            account.setBirthDate(LocalDate.now());
            account.setGender(true);
            accountRepository.save(account);

            var role = roleRepository.findRoleByName("ADMIN").orElseThrow(
                    () -> new RoleException(RoleError.ROLE_NOT_FOUND)
            );
            AccountRole ar = new AccountRole(account, role);
            account.addAccountRole(ar);

            accountRepository.save(account);
        }
    }
}
