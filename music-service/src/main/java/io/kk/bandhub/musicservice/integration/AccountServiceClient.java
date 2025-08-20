package io.kk.bandhub.musicservice.integration;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.kk.bandhub.musicservice.dto.AccountDTO;
import io.kk.bandhub.musicservice.exception.RegistrationException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "ACCOUNT-SERVICE")
public interface AccountServiceClient {

    @GetMapping("/account/internal/{id}")
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallbackGetAccount")
    AccountDTO getAccount(@PathVariable Integer id);

    @GetMapping("/account/internal/{id}")
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallbackGetAccountInRegistration")
    AccountDTO getAccountInRegistration(@PathVariable Integer id);

    @GetMapping("/account/internal/search")
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallbackGetAccountsWithParams")
    List<AccountDTO> getAccountsWithParams(@RequestParam(required = false) String firstName,
                                           @RequestParam(required = false) String lastName,
                                           @RequestParam(required = false) Integer minAge,
                                           @RequestParam(required = false) Integer maxAge,
                                           @RequestParam(required = false) List<String> cities);

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
        if (throwable instanceof feign.FeignException.ServiceUnavailable) {
            throw new RegistrationException("Registration failed because of service unavailability");
        } else if (throwable instanceof feign.FeignException.NotFound) {
            throw new RegistrationException("Registration failed because of bad data (user does not exist)");
        } else {
            throw new RegistrationException("Registration failed, something went wrong");
        }
    }

    default List<AccountDTO> fallbackGetAccountsWithParams(Throwable throwable) {
        throw new RuntimeException("Outer service error");
    }
}
