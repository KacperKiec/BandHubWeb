package io.kk.bandhub.venueservice.service;

import io.kk.bandhub.venueservice.dto.*;
import io.kk.bandhub.venueservice.exception.offer.OfferError;
import io.kk.bandhub.venueservice.exception.offer.OfferException;
import io.kk.bandhub.venueservice.model.Booking;
import io.kk.bandhub.venueservice.model.OfferStatus;
import io.kk.bandhub.venueservice.repository.booking.BookingRepository;
import io.kk.bandhub.venueservice.repository.booking.BookingSpecification;
import io.kk.bandhub.venueservice.repository.offer.OfferRepository;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final OfferRepository offerRepository;
    private final RabbitTemplate rabbitTemplate;
    private final Mapper mapper;

    public BookingService(BookingRepository bookingRepository, OfferRepository offerRepository, RabbitTemplate rabbitTemplate, Mapper mapper) {
        this.bookingRepository = bookingRepository;
        this.offerRepository = offerRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.mapper = mapper;
    }

    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        Booking booking = new Booking();
        booking.setBookingDate(LocalDate.now());
        booking.setTenantId(bookingDTO.getTenantId());

        var offer = offerRepository.findById(bookingDTO.getOfferId()).orElseThrow(
                () -> new OfferException(OfferError.OFFER_NOT_FOUND)
        );
        if(offer.getStatus() != OfferStatus.active) {
            throw new OfferException(OfferError.OFFER_NOT_FOUND);
        }
        offer.setStatus(OfferStatus.claimed);
        booking.setOffer(offer);
        offerRepository.save(offer);
        var mappedBooking = mapper.toBookingDTO(bookingRepository.save(booking));
        mappedBooking.setToPublish(bookingDTO.getToPublish());

        if(bookingDTO.getToPublish()) {
            rabbitTemplate.convertAndSend("event-queue", bookingDTO.getEvent());
        }

        return mappedBooking;
    }

    public void deleteBooking(Integer bookingId) {
        var existingBooking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new OfferException(OfferError.OFFER_NOT_FOUND)
        );
        bookingRepository.deleteById(bookingId);
    }

    public BookingDTO getBooking(Integer bookingId) {
        return mapper.toBookingDTO(bookingRepository.findById(bookingId).orElseThrow(
                () -> new OfferException(OfferError.OFFER_NOT_FOUND)
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
}
