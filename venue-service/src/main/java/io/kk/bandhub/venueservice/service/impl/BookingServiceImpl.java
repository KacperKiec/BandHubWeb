package io.kk.bandhub.venueservice.service.impl;

import io.kk.bandhub.venueservice.dto.*;
import io.kk.bandhub.venueservice.exception.booking.BookingError;
import io.kk.bandhub.venueservice.exception.booking.BookingException;
import io.kk.bandhub.venueservice.exception.offer.OfferError;
import io.kk.bandhub.venueservice.exception.offer.OfferException;
import io.kk.bandhub.venueservice.model.Booking;
import io.kk.bandhub.venueservice.model.Offer;
import io.kk.bandhub.venueservice.service.BookingService;
import io.kk.bandhub.venueservice.service.OfferAvailabilityService;
import io.kk.bandhub.venueservice.integration.RabbitService;
import io.kk.bandhub.venueservice.utils.OfferStatus;
import io.kk.bandhub.venueservice.repository.booking.BookingRepository;
import io.kk.bandhub.venueservice.repository.booking.BookingSpecification;
import io.kk.bandhub.venueservice.repository.offer.OfferRepository;
import io.kk.bandhub.venueservice.utils.TimeRange;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final OfferRepository offerRepository;
    private final OfferAvailabilityService offerAvailabilityService;
    private final RabbitService rabbitService;
    private final Mapper mapper;

    @Transactional
    public BookingDTO createBooking(BookingRequestDTO bookingDTO) {
        Booking booking = new Booking();
        booking.setBookingDate(LocalDate.now());
        booking.setTenantId(bookingDTO.getTenantId());

        var offer = offerRepository.findById(bookingDTO.getOfferId()).orElseThrow(
                () -> new OfferException(OfferError.OFFER_NOT_FOUND)
        );

        if(offer.getStatus() != OfferStatus.ACTIVE) {
            throw new OfferException(OfferError.OFFER_NOT_FOUND);
        }

        if (offerAvailabilityService.isAvailable(offer, new TimeRange(bookingDTO.getStartTime(), bookingDTO.getEndTime()))) {
            booking.setOffer(offer);
            bookingRepository.save(booking);

            offer = offerAvailabilityService.setAvailabilityRanges(offer);
            offerRepository.save(offer);

            sendNotification(offer, booking);
            return mapper.toBookingDTO(booking);
        } else {
            throw new BookingException(BookingError.OFFER_ALREADY_CLAIMED);
        }
    }

    @Transactional
    public void deleteBooking(Integer bookingId) {
        var existingBooking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new OfferException(OfferError.OFFER_NOT_FOUND)
        );

        var offer = existingBooking.getOffer();
        bookingRepository.deleteById(bookingId);

        offer = offerAvailabilityService.setAvailabilityRanges(offer);
        offerRepository.save(offer);
    }

    public BookingDTO getBooking(Integer bookingId) {
        return mapper.toBookingDTO(bookingRepository.findById(bookingId).orElseThrow(
                () -> new BookingException(BookingError.BOOKING_NOT_FOUND)
        ));
    }

    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream().map(mapper::toBookingDTO).toList();
    }

    public Page<BookingDTO> getBookings(
            Pageable pageable,
            Integer tenantId,
            Integer offerId,
            OfferStatus status) {

        Specification<Booking> spec = BookingSpecification.hasOfferId(offerId)
                .and(BookingSpecification.hasTenantId(tenantId))
                .and(BookingSpecification.hasStatus(status));

        return bookingRepository.findAll(spec, pageable).map(mapper::toBookingDTO);
    }

    private void sendNotification(Offer offer, Booking booking) {
        NotificationRequestDTO dto = new NotificationRequestDTO();
        dto.setType(NotificationType.NEW_BOOKING);
        dto.setRecipientId(offer.getVenue().getOwnerId());
        dto.setTitle("Nowa rezerwacja");
        dto.setContent("Ktoś zarezerwował miejsce w %s na dzień %s.".formatted(offer.getVenue().getName(), booking.getBookingDate().toString()));

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("tenantId", booking.getTenantId());
        dto.setMetadata(metadata);

        rabbitService.sendNotification(dto);
    }
}
