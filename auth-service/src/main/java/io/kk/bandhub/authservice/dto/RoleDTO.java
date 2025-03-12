package io.kk.bandhub.authservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
public class RoleDTO implements GrantedAuthority {
    private Integer id;
    private String name;

    @Override
    public String getAuthority() {
        return getName();
    }
}
