package io.kk.bandhub.musicservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.kk.bandhub.musicservice.dto.AccountDTO;
import io.kk.bandhub.musicservice.dto.RegistrationException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;

@FeignClient(name = "ACCOUNT-SERVICE")
public interface AccountServiceClient {

    @GetMapping("/account/{id}")
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallbackGetAccount")
    AccountDTO getAccount(@PathVariable Integer id);

    @GetMapping("/account/{id}")
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallbackGetAccountInRegistration")
    AccountDTO getAccountInRegistration(@PathVariable Integer id);

    default AccountDTO fallbackGetAccount(Integer id, Throwable throwable) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(id);
        accountDTO.setFirstName("unknown");
        accountDTO.setLastName("unknown");
        accountDTO.setGender(null);
        accountDTO.setBirthDate(LocalDate.now());
        accountDTO.setCity("unknown");
        return accountDTO;
    }

    default AccountDTO fallbackGetAccountInRegistration(Throwable throwable) {
        throw new RegistrationException("Registration failed because of service unavailability");
    }

}
