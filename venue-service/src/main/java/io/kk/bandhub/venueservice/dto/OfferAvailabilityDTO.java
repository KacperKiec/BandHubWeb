package io.kk.bandhub.venueservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class OfferAvailabilityDTO {
    private LocalTime startTime;
    private LocalTime endTime;
}
