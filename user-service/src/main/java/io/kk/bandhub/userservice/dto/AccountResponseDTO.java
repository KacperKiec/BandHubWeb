package io.kk.bandhub.userservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AccountResponseDTO {
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Boolean gender;
    private String city;
    private String country;
}
