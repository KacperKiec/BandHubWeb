package io.kk.bandhub.eventservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class EventDTO {
    private Integer id;
    private String name;
    private Integer bandId;
    private String bandName;
    private Integer venueId;
    private String venueName;

    private String street;
    private String number;
    private String city;
    private String postalCode;
    private String country;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String description;
    private Double ticketPrice;
}
