package io.kk.bandhub.eventservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.kk.bandhub.eventservice.dto.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;

@FeignClient(name = "ACCOUNT-SERVICE")
public interface AccountServiceClient {

    @GetMapping("/account/{id}")
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallbackGetAccount")
    AccountDTO getAccount(@PathVariable Integer id);

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
}
