package io.kk.bandhub.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AccountUpdateDTO {
    @NotNull
    private Integer id;

    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private Boolean gender;

    @NotNull
    @NotBlank
    private String city;

    @NotNull
    @NotBlank
    private String country;
}
