package io.kk.bandhub.venueservice;

import io.kk.bandhub.venueservice.model.Booking;
import io.kk.bandhub.venueservice.model.Offer;
import io.kk.bandhub.venueservice.model.OfferAvailability;
import io.kk.bandhub.venueservice.repository.booking.BookingRepository;
import io.kk.bandhub.venueservice.service.OfferAvailabilityService;
import io.kk.bandhub.venueservice.utils.TimeRange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OfferAvailabilityServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private OfferAvailabilityService offerAvailabilityService;

    private Offer createTestOffer() {
        Offer offer = new Offer();
        offer.setId(1);
        offer.setStartTime(LocalTime.of(10, 0));
        offer.setEndTime(LocalTime.of(18, 0));

        OfferAvailability initialAvailability = new OfferAvailability(
                offer, LocalTime.of(10, 0), LocalTime.of(18, 0));
        offer.setAvailabilityRanges(new TreeSet<>(Set.of(initialAvailability)));

        Booking booking = new Booking();
        booking.setOffer(offer);
        booking.setStartTime(LocalTime.of(12, 0));
        booking.setEndTime(LocalTime.of(14, 0));

        when(bookingRepository.findAllByOfferId(1)).thenReturn(new ArrayList<>(List.of(booking)));

        return offer;
    }


    @Test
    void shouldCutAvailabilityAroundExistingBooking() {
        // given
        Offer offer = createTestOffer();

        // when
        Offer result = offerAvailabilityService.setAvailabilityRanges(offer);

        // then
        Set<OfferAvailability> availabilities = result.getAvailabilityRanges();
        assertEquals(2, availabilities.size());

        List<OfferAvailability> sorted = availabilities.stream().sorted(Comparator.comparing(OfferAvailability::getStartTime)).toList();

        assertEquals(LocalTime.of(10, 0), sorted.get(0).getStartTime());
        assertEquals(LocalTime.of(12, 0), sorted.get(0).getEndTime());

        assertEquals(LocalTime.of(14, 0), sorted.get(1).getStartTime());
        assertEquals(LocalTime.of(18, 0), sorted.get(1).getEndTime());
    }

    @Test
    void shouldReturnTrue() {
        // given
        Offer offer = createTestOffer();
        offer = offerAvailabilityService.setAvailabilityRanges(offer);

        // when
        Boolean result = offerAvailabilityService.isAvailable(offer, new TimeRange(LocalTime.of(10, 0), LocalTime.of(12, 0)));

        // then
        assertEquals(true, result);
    }

    @Test
    void shouldReturnFalseCauseBooking() {
        // given
        Offer offer = createTestOffer();
        offer = offerAvailabilityService.setAvailabilityRanges(offer);

        // when
        Boolean result = offerAvailabilityService.isAvailable(offer, new TimeRange(LocalTime.of(11, 0), LocalTime.of(13, 0)));

        // then
        assertEquals(false, result);
    }

    @Test
    void shouldReturnFalseCauseOfferTimeFrames() {
        // given
        Offer offer = createTestOffer();

        offer = offerAvailabilityService.setAvailabilityRanges(offer);

        // when
        Boolean result = offerAvailabilityService.isAvailable(offer, new TimeRange(LocalTime.of(9, 0), LocalTime.of(13, 0)));

        // then
        assertEquals(false, result);
    }

    @Test
    void shouldAddAvailability() {
        // given
        Offer offer = createTestOffer();
        offer = offerAvailabilityService.setAvailabilityRanges(offer);
        assertEquals(2, offer.getAvailabilityRanges().size());

        // when
        when(bookingRepository.findAllByOfferId(offer.getId())).thenReturn(Collections.emptyList());
        offer = offerAvailabilityService.setAvailabilityRanges(offer);

        // then
        Set<OfferAvailability> availabilities = offer.getAvailabilityRanges();
        assertEquals(1, availabilities.size());

        OfferAvailability availability = availabilities.iterator().next();
        assertEquals(LocalTime.of(10, 0), availability.getStartTime());
        assertEquals(LocalTime.of(18, 0), availability.getEndTime());
    }
}

