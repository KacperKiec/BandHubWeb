package io.kk.bandhub.venueservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VenueDTO {
    private Integer id;
    private Integer ownerId;
    private String name;
    private Integer capacity;
    private String description;
    private String street;
    private String number;
    private String city;
    private String postalCode;
    private String country;
}
