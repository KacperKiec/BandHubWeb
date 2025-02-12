package io.kk.bandhub.userservice.controller;

import io.kk.bandhub.userservice.dto.AccountDTO;
import io.kk.bandhub.userservice.dto.RoleDTO;
import io.kk.bandhub.userservice.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountDTO> addAccount(@RequestBody @Valid AccountDTO accountDTO) {
        var result = accountService.save(accountDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Integer id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<AccountDTO> updateAccount(@RequestBody @Valid AccountDTO accountDTO) {
        var result = accountService.update(accountDTO);
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

    @GetMapping(value = "/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable Integer id) {
        var result = accountService.findById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        return ResponseEntity.ok(accountService.findAll());
    }

    @GetMapping(value = "/paged")
    public ResponseEntity<Page<AccountDTO>> getAllAccounts(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable ) {

        return ResponseEntity.ok(accountService.findAll(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<List<AccountDTO>> findAllWithSpec(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) Boolean gender,
            @RequestParam(required = false) List<String> cities,
            @RequestParam(required = false) List<RoleDTO> roles) {

        var accounts = accountService.findAllWithSpec(email, firstName, lastName, minAge, maxAge, gender, cities, roles);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/search/paged")
    public ResponseEntity<Page<AccountDTO>> findAllWithSpecPaged(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) Boolean gender,
            @RequestParam(required = false) List<String> cities,
            @RequestParam(required = false) List<RoleDTO> roles,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        var accounts = accountService.findAllWithSpec(email, firstName, lastName, minAge, maxAge, gender, cities, roles, pageable);
        return ResponseEntity.ok(accounts);
    }

}
