package io.kk.bandhub.userservice.repository;

import io.kk.bandhub.userservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAccountRepository extends JpaRepository<Account, Integer>, AccountRepository, JpaSpecificationExecutor<Account> {

}
