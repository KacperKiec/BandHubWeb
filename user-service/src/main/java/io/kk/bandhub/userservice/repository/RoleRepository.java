package io.kk.bandhub.userservice.repository;

import io.kk.bandhub.userservice.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {
    Role save(Role role);
    void deleteById(Integer id);
    Optional<Role> findRoleById(Integer id);
    Optional<Role> findRoleByName(final String name);
    List<Role> findAll();
}
