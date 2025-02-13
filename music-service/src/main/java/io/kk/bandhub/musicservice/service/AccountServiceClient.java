package io.kk.bandhub.musicservice.service;

import io.kk.bandhub.musicservice.dto.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ACCOUNT-SERVICE")
public interface AccountServiceClient {

    @GetMapping("/account/{id}")
    AccountDTO getAccount(@PathVariable Integer id);
}
