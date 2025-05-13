package io.kk.bandhub.userservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class AccountRequestDTO {
    @Size(max = 50)
    @NotNull
    private String email;

    @Size(max = 50)
    @NotNull
    private String password;

    @Size(max = 30)
    @NotNull
    private String firstName;

    @Size(max = 30)
    @NotNull
    private String lastName;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private Boolean gender;

    @Size(max = 50)
    private String city;

    @Size(max = 50)
    private String country;

    private Set<RoleDTO> roles = new HashSet<>();
}
