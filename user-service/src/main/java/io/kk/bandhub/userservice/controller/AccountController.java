package io.kk.bandhub.userservice.controller;

import io.kk.bandhub.userservice.dto.*;
import io.kk.bandhub.userservice.exception.AccountError;
import io.kk.bandhub.userservice.exception.AccountException;
import io.kk.bandhub.userservice.service.AccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/account")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Void> addAccount(@RequestBody @Valid AccountRequestDTO accountRequestDTO) {
        accountService.save(accountRequestDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Integer id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<AccountResponseDTO> updateAccount(@RequestBody @Valid AccountUpdateDTO accountUpdateDTO, Authentication authentication) {
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
        Jwt jwt = jwtAuth.getToken();

        Long userIdFromToken = jwt.getClaim("userId");

        if (!userIdFromToken.equals(accountUpdateDTO.getId().longValue())) {
            throw new AccountException(AccountError.ACCESS_DENIED);
        }

        var result = accountService.update(accountUpdateDTO);
        return ResponseEntity.ok(result);
    }

    @PatchMapping(value = "/role/{id}")
    public ResponseEntity<Void> addRole(@RequestBody RoleDTO roleDTO, @PathVariable Integer id) {
        accountService.addRole(id, roleDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/role/{id}")
    public ResponseEntity<Void> deleteRole(@RequestBody RoleDTO roleDTO, @PathVariable Integer id) {
        accountService.removeRole(id, roleDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/current")
    public ResponseEntity<AccountResponseDTO> getCurrentAccount(Authentication authentication) {
        String email;
        try {
            email = authentication.getName();
        } catch (NullPointerException e) {
            throw new AccountException(AccountError.ACCOUNT_NOT_FOUND);
        }
        var result = accountService.findByEmail(email);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/auth")
    public ResponseEntity<AccountAuthDTO> getAccountToAuth(@RequestParam String email) {
        var result = accountService.findToAuth(email);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AccountResponseDTO> getAccount(@PathVariable Integer id) {
        var result = accountService.findById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/email")
    public ResponseEntity<AccountResponseDTO> getAccountByEmail(@RequestParam String email) {
        var result = accountService.findByEmail(email);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts() {
        return ResponseEntity.ok(accountService.findAll());
    }

    @GetMapping(value = "/paged")
    public ResponseEntity<Page<AccountResponseDTO>> getAllAccounts(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable ) {

        return ResponseEntity.ok(accountService.findAll(pageable));
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

}
