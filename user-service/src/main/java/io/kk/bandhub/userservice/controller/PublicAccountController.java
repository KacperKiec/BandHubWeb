package io.kk.bandhub.userservice.controller;

import io.kk.bandhub.userservice.dto.AccountRequestDTO;
import io.kk.bandhub.userservice.service.AccountService;
import io.kk.bandhub.userservice.service.AuthAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/account/public")
@RestController
@RequiredArgsConstructor
public class PublicAccountController {

    private final AccountService accountService;
    private final AuthAccountService authAccountService;

    @PostMapping
    ResponseEntity<Void> addAccount(@RequestBody @Valid AccountRequestDTO accountRequestDTO) {
        accountService.save(accountRequestDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/remind")
    ResponseEntity<Void> remindPassword(@RequestBody Map<String, String> body) {
        authAccountService.remindPassword(body.get("email"));
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
