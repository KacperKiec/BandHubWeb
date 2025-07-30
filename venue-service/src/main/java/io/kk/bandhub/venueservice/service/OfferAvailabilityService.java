package io.kk.bandhub.venueservice.service;

import io.kk.bandhub.venueservice.model.Offer;
import io.kk.bandhub.venueservice.utils.TimeRange;

import java.time.LocalDate;

public interface OfferAvailabilityService {
    Boolean isPossibleToCreate(TimeRange timeRange, LocalDate date, Integer venueId, Integer existingOfferId);
    Boolean isAvailable(Offer offer, TimeRange timeRange);
    Offer setAvailabilityRanges(Offer offer);
    Boolean checkChangingPossibility(Offer offer);
}
