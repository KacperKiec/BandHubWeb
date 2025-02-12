package io.kk.bandhub.userservice.repository;

import io.kk.bandhub.userservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaRoleRepository extends JpaRepository<Role, Integer>, RoleRepository{

}
