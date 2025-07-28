package io.kk.bandhub.venueservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VenueUpdateRequestDTO {
    @NotNull
    private Integer id;

    @NotNull
    private Integer ownerId;

    @Size(max = 40)
    @NotNull
    private String name;

    @NotNull
    private Integer capacity;

    @Size(max = 150)
    private String description;

    @Size(max = 50)
    @NotNull
    private String street;

    @Size(max = 10)
    @NotNull
    private String number;

    @Size(max = 50)
    @NotNull
    private String city;

    @Size(max = 15)
    @NotNull
    private String postalCode;

    @Size(max = 50)
    @NotNull
    private String country;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;
}
