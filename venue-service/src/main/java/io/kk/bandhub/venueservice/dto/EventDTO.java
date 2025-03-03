package io.kk.bandhub.venueservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class EventDTO {
    private Integer organizerId;
    private Integer bandId;
    private Integer venueId;
    private String name;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String description;
    private Double ticketPrice;
}
