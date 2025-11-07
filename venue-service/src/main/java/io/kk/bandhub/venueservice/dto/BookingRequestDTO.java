package io.kk.bandhub.venueservice.dto;

import io.kk.bandhub.venueservice.utils.validation.BookingTimeValidatable;
import io.kk.bandhub.venueservice.utils.validation.ValidBookingTimes;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@ValidBookingTimes
public class BookingRequestDTO implements BookingTimeValidatable {

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
