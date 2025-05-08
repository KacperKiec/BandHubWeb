package io.kk.bandhub.userservice.dto;

import io.kk.bandhub.userservice.model.Account;
import io.kk.bandhub.userservice.model.AccountRole;
import io.kk.bandhub.userservice.model.Role;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class Mapper {

    public RoleDTO toRoleDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        return roleDTO;
    }

    public AccountDTO toAccountDTO(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setEmail(account.getEmail());
        accountDTO.setPassword(account.getPassword());
        accountDTO.setFirstName(account.getFirstName());
        accountDTO.setLastName(account.getLastName());
        accountDTO.setGender(account.getGender());
        accountDTO.setBirthDate(account.getBirthDate());
        accountDTO.setCity(account.getCity());
        accountDTO.setCountry(account.getCountry());

        var roles = account.getAccountRoles().stream().map(AccountRole::getRole).map(this::toRoleDTO).collect(Collectors.toSet());
        accountDTO.setRoles(roles);
        return accountDTO;
    }
}
