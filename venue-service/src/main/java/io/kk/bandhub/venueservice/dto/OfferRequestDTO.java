package io.kk.bandhub.venueservice.dto;

import io.kk.bandhub.venueservice.utils.OfferStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class OfferRequestDTO {

    @NotNull
    private Integer venueId;

    @NotNull
    private Double pricePerHour;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @NotNull
    private OfferStatus status;
}
