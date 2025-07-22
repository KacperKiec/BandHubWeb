package io.kk.bandhub.venueservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class EventDataDTO {
    private Integer venueId;
    private String venueName;
    private Integer tenantId;
    private LocalTime startTime;
    private LocalTime endTime;

    private String street;
    private String number;
    private String city;
    private String postalCode;
    private String country;
}
