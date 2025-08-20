package io.kk.bandhub.userservice.service;

import io.kk.bandhub.userservice.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountService {
    void save(AccountRequestDTO accountRequestDTO);
    void delete(Integer id);
    AccountResponseDTO update(AccountUpdateDTO accountUpdateDTO);
    AccountResponseDTO findById(Integer id);
    AccountResponseDTO findByEmail(String email);
    List<AccountResponseDTO> findAll();
    Page<AccountResponseDTO> findAll(Pageable pageable);
    List<AccountResponseDTO> findAllWithSpec(String email, String firstName, String lastName, Integer minAge, Integer maxAge, Boolean gender, List<String> cities, List<String> roles);
    Page<AccountResponseDTO> findAllWithSpec(String email, String firstName, String lastName, Integer minAge, Integer maxAge, Boolean gender, List<String> cities, List<String> roles, Pageable pageable);
    void addRole(Integer id, RoleDTO roleDTO);
    void removeRole(Integer id, RoleDTO roleDTO);
    Page<AccountResponseDTO> findByNames(String firstName, String lastName, Pageable pageable);
}
