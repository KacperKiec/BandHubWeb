package io.kk.bandhub.venueservice.dto;

import io.kk.bandhub.venueservice.utils.OfferStatus;
import io.kk.bandhub.venueservice.utils.OfferTimeValidatable;
import io.kk.bandhub.venueservice.utils.ValidOfferTimes;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@ValidOfferTimes
public class OfferUpdateRequestDTO implements OfferTimeValidatable {

    @NotNull
    private Integer id;

    @NotNull
    private Integer venueId;

    @NotNull
    private BigDecimal pricePerHour;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @NotNull
    private OfferStatus status;
}
