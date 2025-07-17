package io.kk.bandhub.venueservice.dto;

import io.kk.bandhub.venueservice.model.OfferAvailability;
import io.kk.bandhub.venueservice.utils.OfferStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
public class OfferDTO {
    private Integer id;
    private Integer venueId;
    private String venueName;
    private Double pricePerHour;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private OfferStatus status;
    private Set<OfferAvailability> availableRanges;
}
