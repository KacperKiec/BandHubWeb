package io.kk.bandhub.venueservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VenueDTO {
    private Integer id;

    @NotNull
    private Integer ownerId;
    private String ownerFirstName;
    private String ownerLastname;

    @NotNull
    private String name;

    @NotNull
    private String city;

    @NotNull
    private String address;

    @NotNull
    private Integer capacity;

    private String description;
}
