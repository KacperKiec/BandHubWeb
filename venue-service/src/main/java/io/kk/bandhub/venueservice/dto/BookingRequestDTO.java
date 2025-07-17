package io.kk.bandhub.venueservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class BookingRequestDTO {

    @NotNull
    private Integer tenantId;

    @NotNull
    private Integer offerId;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @NotNull
    private LocalDate bookingDate;
}
