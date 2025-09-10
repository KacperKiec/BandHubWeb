package io.kk.bandhub.musicservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AccountDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Boolean gender;
    private String city;
    private String country;
    private String imageURL;
}
