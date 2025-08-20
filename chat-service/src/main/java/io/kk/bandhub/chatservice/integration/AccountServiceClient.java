package io.kk.bandhub.chatservice.integration;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.kk.bandhub.chatservice.dto.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ACCOUNT-SERVICE")
public interface AccountServiceClient {

    @GetMapping("/account/internal/{id}")
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallbackGetAccount")
    AccountDTO getAccount(@PathVariable Integer id);

    default AccountDTO fallbackGetAccount(Integer id, Throwable throwable) {
        return AccountDTO.builder()
                .id(id)
                .firstName("unknown")
                .lastName("unknown")
                .build();
    }

}
