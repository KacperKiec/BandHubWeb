package io.kk.bandhub.userservice.controller;

import io.kk.bandhub.userservice.dto.AccountResponseDTO;
import io.kk.bandhub.userservice.dto.AccountUpdateDTO;
import io.kk.bandhub.userservice.dto.RoleDTO;
import io.kk.bandhub.userservice.exception.AccountError;
import io.kk.bandhub.userservice.exception.AccountException;
import io.kk.bandhub.userservice.service.AccountService;
import io.kk.bandhub.userservice.service.AuthAccountService;
import io.kk.bandhub.userservice.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AuthAccountService authAccountService;

    @PutMapping
    public ResponseEntity<AccountResponseDTO> updateAccount(@RequestBody @Valid AccountUpdateDTO accountUpdateDTO, Authentication authentication) {
        SecurityUtils.verifyAuthorities(authentication, accountUpdateDTO.getId());

        var result = accountService.update(accountUpdateDTO);
        return ResponseEntity.ok(result);
    }

    @PatchMapping(value = "/image")
    public ResponseEntity<Void> updateImage(@RequestBody Map<String, String> imageURL, Authentication authentication) {
        var uid = SecurityUtils.getUserIdFromAuthentication(authentication);
        accountService.setImageURL(uid.intValue(), imageURL.get("url"));
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping(value = "/password")
    public ResponseEntity<Boolean> validatePassword(@RequestBody String password, Authentication authentication) {
        var uid = SecurityUtils.getUserIdFromAuthentication(authentication);

        return ResponseEntity.ok(authAccountService.validatePassword(uid.intValue(), password));
    }

    @PatchMapping(value = "/password")
    public ResponseEntity<Void> changePassword(@RequestBody String password, Authentication authentication) {
        var uid = SecurityUtils.getUserIdFromAuthentication(authentication);

        authAccountService.changePassword(uid.intValue(), password);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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

    @GetMapping(value = "/name")
    public ResponseEntity<Page<AccountResponseDTO>> findByName(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        var result = accountService.findByNames(firstName, lastName, pageable);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Integer id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
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
}
