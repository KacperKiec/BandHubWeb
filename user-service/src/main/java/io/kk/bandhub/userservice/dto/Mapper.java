package io.kk.bandhub.userservice.dto;

import io.kk.bandhub.userservice.model.Account;
import io.kk.bandhub.userservice.model.Role;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public RoleDTO toRoleDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        return roleDTO;
    }

    public AccountResponseDTO toAccountDTO(Account account) {
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
        accountResponseDTO.setId(account.getId());
        accountResponseDTO.setEmail(account.getEmail());
        accountResponseDTO.setFirstName(account.getFirstName());
        accountResponseDTO.setLastName(account.getLastName());
        accountResponseDTO.setGender(account.getGender());
        accountResponseDTO.setBirthDate(account.getBirthDate());
        accountResponseDTO.setCity(account.getCity());
        accountResponseDTO.setCountry(account.getCountry());

        return accountResponseDTO;
    }
}
