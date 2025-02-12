package io.kk.bandhub.userservice.repository;

import io.kk.bandhub.userservice.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    Account save(Account account);
    void deleteById(Integer id);
    Optional<Account> findAccountById(Integer id);
    Optional<Account> findAccountByEmail(String email);
    List<Account> findAll();
    Page<Account> findAll(Pageable pageable);
    List<Account> findAll(Specification<Account> spec);
    Page<Account> findAll(Specification<Account> spec, Pageable pageable);
}
