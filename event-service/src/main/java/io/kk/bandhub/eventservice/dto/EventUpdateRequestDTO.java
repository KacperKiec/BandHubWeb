package io.kk.bandhub.eventservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class EventUpdateRequestDTO {
    @NotNull
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private Integer creatorId;

    @NotNull
    private Integer bandId;

    private Integer bookingId;
    private Integer venueId;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    private Double ticketPrice;
}
