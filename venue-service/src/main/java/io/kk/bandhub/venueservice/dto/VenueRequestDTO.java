package io.kk.bandhub.venueservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VenueRequestDTO {
    @NotNull
    private Integer ownerId;

    @Size(max = 40)
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private Integer capacity;

    @Size(max = 150)
    private String description;

    @Size(max = 50)
    @NotNull
    @NotBlank
    private String street;

    @Size(max = 10)
    @NotNull
    @NotBlank
    private String number;

    @Size(max = 50)
    @NotNull
    @NotBlank
    private String city;

    @Size(max = 15)
    @NotNull
    @NotBlank
    private String postalCode;

    @Size(max = 50)
    @NotNull
    @NotBlank
    private String country;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;
}
