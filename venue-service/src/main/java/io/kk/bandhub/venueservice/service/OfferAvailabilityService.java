package io.kk.bandhub.venueservice.service;

import io.kk.bandhub.venueservice.model.Offer;
import io.kk.bandhub.venueservice.utils.TimeRange;

public interface OfferAvailabilityService {
    Boolean isAvailable(Offer offer, TimeRange timeRange);
    Offer setAvailabilityRanges(Offer offer);
}
