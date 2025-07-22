package io.kk.bandhub.userservice.service.impl;

import io.kk.bandhub.userservice.dto.*;
import io.kk.bandhub.userservice.exception.AccountError;
import io.kk.bandhub.userservice.exception.AccountException;
import io.kk.bandhub.userservice.exception.RoleError;
import io.kk.bandhub.userservice.exception.RoleException;
import io.kk.bandhub.userservice.model.Account;
import io.kk.bandhub.userservice.model.AccountRole;
import io.kk.bandhub.userservice.repository.AccountRepository;
import io.kk.bandhub.userservice.repository.AccountSpecification;
import io.kk.bandhub.userservice.repository.RoleRepository;
import io.kk.bandhub.userservice.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Mapper mapper;

    @Transactional
    public void save(AccountRequestDTO accountRequestDTO) {
        var existingAccount = accountRepository.findAccountByEmail(accountRequestDTO.getEmail());

        if (existingAccount.isPresent()) {
            throw new AccountException(AccountError.ACCOUNT_ALREADY_EXISTS);
        }

        Account account = new Account();
        account.setEmail(accountRequestDTO.getEmail());
        account.setPassword(passwordEncoder.encode(accountRequestDTO.getPassword()));
        account.setFirstName(accountRequestDTO.getFirstName());
        account.setLastName(accountRequestDTO.getLastName());
        account.setBirthDate(accountRequestDTO.getBirthDate());
        account.setGender(accountRequestDTO.getGender());
        account.setCity(accountRequestDTO.getCity());
        account.setCountry(accountRequestDTO.getCountry());

        for (RoleDTO role : accountRequestDTO.getRoles()) {
            var existingRole = roleRepository.findRoleByName(role.getName()).orElseThrow(
                    () -> new RoleException(RoleError.ROLE_NOT_FOUND)
            );
            AccountRole accountRole = new AccountRole(account, existingRole);
            account.addAccountRole(accountRole);
        }

        accountRepository.save(account);
    }

    public void delete(Integer id) {
        accountRepository.findAccountById(id).orElseThrow(
                () -> new AccountException(AccountError.ACCOUNT_NOT_FOUND)
        );
        accountRepository.deleteById(id);
    }

    @Transactional
    public AccountResponseDTO update(AccountUpdateDTO accountUpdateDTO) {
        var existingAccount = accountRepository.findAccountById(accountUpdateDTO.getId()).orElseThrow(
                () -> new AccountException(AccountError.ACCOUNT_NOT_FOUND)
        );

        existingAccount.setFirstName(accountUpdateDTO.getFirstName());
        existingAccount.setLastName(accountUpdateDTO.getLastName());
        existingAccount.setBirthDate(accountUpdateDTO.getBirthDate());
        existingAccount.setGender(accountUpdateDTO.getGender());
        existingAccount.setCity(accountUpdateDTO.getCity());
        existingAccount.setCountry(accountUpdateDTO.getCountry());
        return mapper.toAccountDTO(accountRepository.save(existingAccount));
    }

    public AccountAuthDTO findToAuth(String email) {
        var existingAccount = accountRepository.findAccountByEmail(email).orElseThrow(
                () -> new AccountException(AccountError.ACCOUNT_NOT_FOUND)
        );


        return AccountAuthDTO.builder()
                        .id(existingAccount.getId())
                        .email(existingAccount.getEmail())
                        .password(existingAccount.getPassword())
                        .roles(existingAccount.getAccountRoles().stream().map(AccountRole::getRole).map(mapper::toRoleDTO).collect(Collectors.toSet()))
                        .build();
    }

    public Boolean validatePassword(Integer userId, String password) {
        var account = accountRepository.findAccountById(userId).orElseThrow(
                () -> new AccountException(AccountError.ACCOUNT_NOT_FOUND)
        );

        return passwordEncoder.matches(password, account.getPassword());
    }

    public void changePassword(Integer userId, String password) {
        var account = accountRepository.findAccountById(userId).orElseThrow(
                () -> new AccountException(AccountError.ACCOUNT_NOT_FOUND)
        );

        account.setPassword(passwordEncoder.encode(password));
        accountRepository.save(account);
    }

    public AccountResponseDTO findById(Integer id) {
        var existingAccount = accountRepository.findAccountById(id).orElseThrow(
                () -> new AccountException(AccountError.ACCOUNT_NOT_FOUND)
        );
        return mapper.toAccountDTO(existingAccount);
    }

    public AccountResponseDTO findByEmail(String email) {
        var existingAccount = accountRepository.findAccountByEmail(email).orElseThrow(
                () -> new AccountException(AccountError.ACCOUNT_NOT_FOUND)
        );
        return mapper.toAccountDTO(existingAccount);
    }

    public List<AccountResponseDTO> findAll() {
        return accountRepository.findAll().stream().map(mapper::toAccountDTO).toList();
    }

    public Page<AccountResponseDTO> findAll(Pageable pageable) {
        return accountRepository.findAll(pageable).map(mapper::toAccountDTO);
    }

    public List<AccountResponseDTO> findAllWithSpec(
            String email,
            String firstName,
            String lastName,
            Integer minAge,
            Integer maxAge,
            Boolean gender,
            List<String> cities,
            List<String> roles ) {

        Specification<Account> nameSpec = Specification
                .where(AccountSpecification.hasName(firstName))
                .or(AccountSpecification.hasName(lastName))
                .or(AccountSpecification.hasFullName(firstName, lastName));

        Specification<Account> spec = Specification.where(AccountSpecification.hasEmail(email))
                .and(nameSpec)
                .and(AccountSpecification.hasAgeBetween(minAge, maxAge))
                .and(AccountSpecification.hasGender(gender))
                .and(AccountSpecification.hasCities(cities))
                .and(AccountSpecification.hasRoles(roles));


        return accountRepository.findAll(spec).stream().map(mapper::toAccountDTO).toList();
    }

    public Page<AccountResponseDTO> findAllWithSpec(
            String email,
            String firstName,
            String lastName,
            Integer minAge,
            Integer maxAge,
            Boolean gender,
            List<String> cities,
            List<String> roles,
            Pageable pageable ) {

        Specification<Account> nameSpec = Specification
                .where(AccountSpecification.hasName(firstName))
                .or(AccountSpecification.hasName(lastName))
                .or(AccountSpecification.hasFullName(firstName, lastName));

        Specification<Account> spec = Specification.where(AccountSpecification.hasEmail(email))
                .and(nameSpec)
                .and(AccountSpecification.hasAgeBetween(minAge, maxAge))
                .and(AccountSpecification.hasGender(gender))
                .and(AccountSpecification.hasCities(cities))
                .and(AccountSpecification.hasRoles(roles));


        return accountRepository.findAll(spec, pageable).map(mapper::toAccountDTO);
    }

    @Transactional
    public void addRole(Integer id, RoleDTO roleDTO) {
        var existingAccount = accountRepository.findAccountById(id).orElseThrow(
                () -> new AccountException(AccountError.ACCOUNT_NOT_FOUND)
        );

        var existingRole = roleRepository.findRoleByName(roleDTO.getName()).orElseThrow(
                () -> new RoleException(RoleError.ROLE_NOT_FOUND)
        );

        var hasRole = existingAccount.getAccountRoles().stream().map(AccountRole::getRole).anyMatch(r -> r.getName().equals(existingRole.getName()));
        if(!hasRole) {
            AccountRole accountRole = new AccountRole(existingAccount, existingRole);
            existingAccount.addAccountRole(accountRole);
        }

        accountRepository.save(existingAccount);
    }

    @Transactional
    public void removeRole(Integer id, RoleDTO roleDTO) {
        var existingAccount = accountRepository.findAccountById(id).orElseThrow(
                () -> new AccountException(AccountError.ACCOUNT_NOT_FOUND)
        );

        var existingRole = roleRepository.findRoleByName(roleDTO.getName()).orElseThrow(
                () -> new RoleException(RoleError.ROLE_NOT_FOUND)
        );

        var roleToRemove = existingAccount.getAccountRoles().stream()
                .filter(accountRole -> accountRole.getRole().getName().equals(existingRole.getName()))
                .findFirst();
        roleToRemove.ifPresent(existingAccount::removeAccountRole);

        accountRepository.save(existingAccount);
    }
}
