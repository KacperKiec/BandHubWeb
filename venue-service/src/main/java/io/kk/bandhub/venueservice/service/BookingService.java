package io.kk.bandhub.venueservice.service;

import io.kk.bandhub.venueservice.dto.BookingDTO;
import io.kk.bandhub.venueservice.dto.BookingRequestDTO;
import io.kk.bandhub.venueservice.utils.OfferStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookingService {
    BookingDTO createBooking(BookingRequestDTO bookingRequestDTO);
    void deleteBooking(Integer bookingId, String initiator);
    BookingDTO getBooking(Integer bookingId);
    List<BookingDTO> getAllBookings();
    List<BookingDTO> getUsableUserBookings(Integer id);
    Page<BookingDTO> getUserBookings(Integer tenantId, Pageable pageable);
    Page<BookingDTO> getOwnerBookings(Integer ownerId, Pageable pageable);
    Page<BookingDTO> getBookings(Pageable pageable, Integer tenantId, Integer offerId, OfferStatus status);
}
