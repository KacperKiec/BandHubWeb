package io.kk.bandhub.userservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AccountUpdateDTO {
    @NotNull
    private Integer id;

    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Boolean gender;
    private String city;
    private String country;
}
