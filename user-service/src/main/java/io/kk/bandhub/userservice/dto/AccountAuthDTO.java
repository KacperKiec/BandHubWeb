package io.kk.bandhub.userservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class AccountAuthDTO {
    private Integer id;
    private String email;
    private String password;
    private Set<RoleDTO> roles;
}
