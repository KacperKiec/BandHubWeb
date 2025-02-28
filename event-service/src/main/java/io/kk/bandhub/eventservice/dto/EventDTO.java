package io.kk.bandhub.eventservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class EventDTO {
    private Integer id;

    @NotNull
    private Integer organizerId;

    private AccountDTO organizerDTO;

    @NotNull
    private Integer venueId;

    private VenueDTO venueDTO;

    @Size(max = 50)
    @NotNull
    private String name;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @Size(max = 255)
    private String description;

    private Double ticketPrice;
}
