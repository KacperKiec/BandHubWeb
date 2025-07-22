package io.kk.bandhub.venueservice.service.impl;

import io.kk.bandhub.venueservice.model.Booking;
import io.kk.bandhub.venueservice.model.Offer;
import io.kk.bandhub.venueservice.model.OfferAvailability;
import io.kk.bandhub.venueservice.repository.booking.BookingRepository;
import io.kk.bandhub.venueservice.service.OfferAvailabilityService;
import io.kk.bandhub.venueservice.utils.OfferStatus;
import io.kk.bandhub.venueservice.utils.TimeRange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OfferAvailabilityServiceImpl implements OfferAvailabilityService {
    private final BookingRepository bookingRepository;

    public Boolean isAvailable(Offer offer, TimeRange timeRange) {
        var availabilities = offer.getAvailabilityRanges();

        for (OfferAvailability availability : availabilities) {
            if (availability.match(timeRange)) return true;
        }
        return false;
    }

    public Offer setAvailabilityRanges(Offer offer) {

        Set<OfferAvailability> availabilities = new TreeSet<>();
        availabilities.add(new OfferAvailability(offer, offer.getStartTime(), offer.getEndTime()));

        offer.setAvailabilityRanges(getAvailabilities(offer, availabilities));
        if (offer.getAvailabilityRanges().isEmpty()) offer.setStatus(OfferStatus.CLAIMED);
        return offer;
    }

    private Set<OfferAvailability> getAvailabilities(Offer offer, Set<OfferAvailability> availabilities) {
        var bookings = bookingRepository.findAllByOfferId(offer.getId());

        bookings.sort(Comparator.comparing(Booking::getStartTime));
        for (Booking booking : bookings) {
            List<OfferAvailability> toDelete = new ArrayList<>();
            List<OfferAvailability> toAdd = new ArrayList<>();
            TimeRange temp = new TimeRange(booking.getStartTime(), booking.getEndTime());

            for (OfferAvailability available : availabilities) {
                if (available.overlaps(temp)) {
                    toDelete.add(available);

                    // before booking
                    if (available.getStartTime().isBefore(temp.getStartTime())) {
                        toAdd.add(new OfferAvailability(offer, available.getStartTime(), temp.getStartTime()));
                    }

                    // after booking
                    if (available.getEndTime().isAfter(temp.getEndTime())) {
                        toAdd.add(new OfferAvailability(offer, temp.getEndTime(), available.getEndTime()));
                    }
                }
            }

            toDelete.forEach(availabilities::remove);
            availabilities.addAll(toAdd);
        }

        return availabilities;
    }
}
