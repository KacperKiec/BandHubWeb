package io.kk.bandhub.eventservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VenueDTO {
    private Integer id;
    private Integer ownerId;
    private AccountDTO ownerDTO;
    private String name;
    private String city;
    private String address;
    private Integer capacity;
    private String description;
}
