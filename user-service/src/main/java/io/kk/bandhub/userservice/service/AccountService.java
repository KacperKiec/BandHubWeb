package io.kk.bandhub.userservice.service;

import io.kk.bandhub.userservice.dto.AccountDTO;
import io.kk.bandhub.userservice.dto.Mapper;
import io.kk.bandhub.userservice.dto.RoleDTO;
import io.kk.bandhub.userservice.exception.AccountError;
import io.kk.bandhub.userservice.exception.AccountException;
import io.kk.bandhub.userservice.exception.RoleError;
import io.kk.bandhub.userservice.exception.RoleException;
import io.kk.bandhub.userservice.model.Account;
import io.kk.bandhub.userservice.model.AccountRole;
import io.kk.bandhub.userservice.model.Role;
import io.kk.bandhub.userservice.repository.AccountRepository;
import io.kk.bandhub.userservice.repository.AccountSpecification;
import io.kk.bandhub.userservice.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final Mapper mapper;

    public AccountService(AccountRepository accountRepository, RoleRepository roleRepository, Mapper mapper) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.mapper = mapper;
    }

    @Transactional
    public AccountDTO save(AccountDTO accountDTO) {
        var existingAccount = accountRepository.findAccountByEmail(accountDTO.getEmail());

        if (existingAccount.isPresent()) {
            throw new AccountException(AccountError.ACCOUNT_ALREADY_EXISTS);
        }

        Account account = new Account();
        setAccountFields(account, accountDTO.getEmail(), accountDTO.getPassword(), accountDTO.getFirstName(), accountDTO.getLastName(), accountDTO.getBirthDate(), accountDTO.getGender(), accountDTO.getCity());

        for (RoleDTO role : accountDTO.getRoles()) {
            var existingRole = roleRepository.findRoleByName(role.getName()).orElseThrow(
                    () -> new RoleException(RoleError.ROLE_NOT_FOUND)
            );
            AccountRole accountRole = new AccountRole(account, existingRole);
            account.addAccountRole(accountRole);
        }

        return mapper.toAccountDTO(accountRepository.save(account));
    }

    public void delete(Integer id) {
        var existingAccount = accountRepository.findAccountById(id).orElseThrow(
                () -> new AccountException(AccountError.ACCOUNT_NOT_FOUND)
        );
        accountRepository.deleteById(id);
    }

    @Transactional
    public AccountDTO update(AccountDTO accountDTO) {
        var existingAccount = accountRepository.findAccountById(accountDTO.getId()).orElseThrow(
                () -> new AccountException(AccountError.ACCOUNT_NOT_FOUND)
        );

        setAccountFields(existingAccount, accountDTO.getEmail(), accountDTO.getPassword(), accountDTO.getFirstName(), accountDTO.getLastName(), accountDTO.getBirthDate(), accountDTO.getGender(), accountDTO.getCity());

        return mapper.toAccountDTO(accountRepository.save(existingAccount));
    }

    private void setAccountFields(Account existingAccount, String email, String password, String firstName, String lastName, LocalDate birthDate, Boolean gender, String city) {
        existingAccount.setEmail(email);
        existingAccount.setPassword(password);
        existingAccount.setFirstName(firstName);
        existingAccount.setLastName(lastName);
        existingAccount.setBirthDate(birthDate);
        existingAccount.setGender(gender);
        existingAccount.setCity(city);
    }

    public AccountDTO findById(Integer id) {
        var existingAccount = accountRepository.findAccountById(id).orElseThrow(
                () -> new AccountException(AccountError.ACCOUNT_NOT_FOUND)
        );
        return mapper.toAccountDTO(existingAccount);
    }

    public List<AccountDTO> findAll() {
        return accountRepository.findAll().stream().map(mapper::toAccountDTO).toList();
    }

    public Page<AccountDTO> findAll(Pageable pageable) {
        return accountRepository.findAll(pageable).map(mapper::toAccountDTO);
    }

    public List<AccountDTO> findAllWithSpec(
            String email,
            String firstName,
            String lastName,
            Integer minAge,
            Integer maxAge,
            Boolean gender,
            List<String> cities,
            List<RoleDTO> roles ) {

        Specification<Account> spec = Specification.where(AccountSpecification.hasEmail(email))
                .and(AccountSpecification.hasFirstName(firstName))
                .and(AccountSpecification.hasLastName(lastName))
                .and(AccountSpecification.hasAgeBetween(minAge, maxAge))
                .and(AccountSpecification.hasGender(gender))
                .and(AccountSpecification.hasCities(cities))
                .and(AccountSpecification.hasRoles(roles));

        return accountRepository.findAll(spec).stream().map(mapper::toAccountDTO).toList();
    }

    public Page<AccountDTO> findAllWithSpec(
            String email,
            String firstName,
            String lastName,
            Integer minAge,
            Integer maxAge,
            Boolean gender,
            List<String> cities,
            List<RoleDTO> roles,
            Pageable pageable ) {

        Specification<Account> spec = Specification.where(AccountSpecification.hasEmail(email))
                .and(AccountSpecification.hasFirstName(firstName))
                .and(AccountSpecification.hasLastName(lastName))
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
