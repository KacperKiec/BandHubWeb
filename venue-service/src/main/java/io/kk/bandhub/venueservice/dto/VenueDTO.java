package io.kk.bandhub.venueservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class VenueDTO {
    private Integer id;
    private Integer ownerId;
    private String name;
    private Integer capacity;
    private LocalTime openTime;
    private LocalTime closeTime;
    private String description;
    private String street;
    private String number;
    private String city;
    private String postalCode;
    private String country;
}
