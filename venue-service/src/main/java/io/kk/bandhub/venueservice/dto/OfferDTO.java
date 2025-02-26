package io.kk.bandhub.venueservice.dto;

import io.kk.bandhub.venueservice.model.OfferStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class OfferDTO {

    private Integer id;

    @NotNull
    private Integer venueId;

    private String venueName;

    @NotNull
    private Double pricePerHour;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    private OfferStatus status;
}
