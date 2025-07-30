package io.kk.bandhub.venueservice.service.impl;

import io.kk.bandhub.venueservice.model.Booking;
import io.kk.bandhub.venueservice.model.Offer;
import io.kk.bandhub.venueservice.model.OfferAvailability;
import io.kk.bandhub.venueservice.repository.booking.BookingRepository;
import io.kk.bandhub.venueservice.repository.offer.OfferRepository;
import io.kk.bandhub.venueservice.service.OfferAvailabilityService;
import io.kk.bandhub.venueservice.utils.OfferStatus;
import io.kk.bandhub.venueservice.utils.TimeRange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OfferAvailabilityServiceImpl implements OfferAvailabilityService {
    private final BookingRepository bookingRepository;
    private final OfferRepository offerRepository;


    /**
     * Funkcja sprawdzająca czy możliwe jest stworzenie oferty takiej
     * żeby nie kolidowała z innymi ofertami
     * */
    public Boolean isPossibleToCreate(TimeRange timeRange, LocalDate date, Integer venueId, Integer existingOfferId) {
        var offers = offerRepository.findOffersByDateAndVenueId(date, venueId);

        for (Offer offer : offers) {
            if (!offer.getEndTime().isBefore(timeRange.getStartTime()) &&
                    !offer.getStartTime().isAfter(timeRange.getEndTime()) &&
                    !(offer.getId().equals(existingOfferId) || offer.getStatus() == OfferStatus.CANCELED)) {
                return false;
            }

        }
        return true;
    }

    public Boolean isAvailable(Offer offer, TimeRange timeRange) {
        var availabilities = offer.getAvailabilityRanges();

        for (OfferAvailability availability : availabilities) {
            if (availability.match(timeRange)) return true;
        }
        return false;
    }

    public Offer setAvailabilityRanges(Offer offer) {

        offer.setAvailabilityRanges(getAvailabilities(offer));
        if (offer.getAvailabilityRanges().isEmpty()) offer.setStatus(OfferStatus.CLAIMED);
        return offer;
    }

    private Set<OfferAvailability> getAvailabilities(Offer offer) {
        var bookings = bookingRepository.findAllByOfferId(offer.getId());

        var availabilities = offer.getAvailabilityRanges();
        availabilities.clear();
        availabilities.add(new OfferAvailability(offer, offer.getStartTime(), offer.getEndTime()));

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

    /**
     * Funkcja sprawdzająca czy dana oferta jest możliwa do edycji
     * ze względu na zaburzenie ram czasowych istniejących rezerwacji
     * */
    public Boolean checkChangingPossibility(Offer offer) {

        var bookings = bookingRepository.findAllByOfferId(offer.getId());

        var earliestBooking = bookings.stream().min(Comparator.comparing(Booking::getStartTime));
        var latestBooking = bookings.stream().max(Comparator.comparing(Booking::getEndTime));

        if (earliestBooking.isPresent() && earliestBooking.get().getStartTime().isBefore(offer.getStartTime())) return false;

        return latestBooking.isEmpty() || !latestBooking.get().getEndTime().isAfter(offer.getEndTime());
    }
}
