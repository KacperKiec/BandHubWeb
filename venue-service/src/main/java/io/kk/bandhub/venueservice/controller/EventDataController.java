package io.kk.bandhub.venueservice.controller;

import io.kk.bandhub.venueservice.dto.EventDataDTO;
import io.kk.bandhub.venueservice.exception.booking.BookingError;
import io.kk.bandhub.venueservice.exception.booking.BookingException;
import io.kk.bandhub.venueservice.repository.booking.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event-data")
@RequiredArgsConstructor
public class EventDataController {
    private final BookingRepository bookingRepository;

    @GetMapping(value = "/{id}")
    ResponseEntity<EventDataDTO> getEventData(@PathVariable Integer id) {
        var booking = bookingRepository.findById(id).orElseThrow(
                () -> new BookingException(BookingError.BOOKING_NOT_FOUND)
        );

        var venue = booking.getOffer().getVenue();

        var data = new EventDataDTO();
        data.setVenueId(venue.getId());
        data.setVenueName(venue.getName());
        data.setTenantId(booking.getTenantId());
        data.setStartTime(booking.getStartTime());
        data.setEndTime(booking.getEndTime());

        data.setStreet(venue.getStreet());
        data.setNumber(venue.getNumber());
        data.setCity(venue.getCity());
        data.setPostalCode(venue.getPostalCode());
        data.setCountry(venue.getCountry());

        return ResponseEntity.ok(data);
    }
}
