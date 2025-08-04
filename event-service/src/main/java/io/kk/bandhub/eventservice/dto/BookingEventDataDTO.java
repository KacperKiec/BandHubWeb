package io.kk.bandhub.eventservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class BookingEventDataDTO {
    private Integer venueId;
    private String venueName;
    private Integer tenantId;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;

    private String street;
    private String number;
    private String city;
    private String postalCode;
    private String country;
}
