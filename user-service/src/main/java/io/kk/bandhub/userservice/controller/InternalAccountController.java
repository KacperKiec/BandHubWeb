package io.kk.bandhub.userservice.controller;

import io.kk.bandhub.userservice.dto.AccountAuthDTO;
import io.kk.bandhub.userservice.dto.AccountResponseDTO;
import io.kk.bandhub.userservice.service.AccountService;
import io.kk.bandhub.userservice.service.AuthAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/account/internal")
@RestController
@RequiredArgsConstructor
public class InternalAccountController {

    private final AccountService accountService;
    private final AuthAccountService authAccountService;

    @GetMapping(value = "/auth")
    public ResponseEntity<AccountAuthDTO> getAccountToAuth(@RequestParam String email) {
        var result = authAccountService.findToAuth(email);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AccountResponseDTO> getAccount(@PathVariable Integer id) {
        var result = accountService.findById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search/paged")
    public ResponseEntity<Page<AccountResponseDTO>> findAllWithSpecPaged(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) Boolean gender,
            @RequestParam(required = false) List<String> cities,
            @RequestParam(required = false) List<String> roles,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        var accounts = accountService.findAllWithSpec(email, firstName, lastName, minAge, maxAge, gender, cities, roles, pageable);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AccountResponseDTO>> findAllWithSpec(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) Boolean gender,
            @RequestParam(required = false) List<String> cities,
            @RequestParam(required = false) List<String> roles) {

        var accounts = accountService.findAllWithSpec(email, firstName, lastName, minAge, maxAge, gender, cities, roles);
        return ResponseEntity.ok(accounts);
    }
}
