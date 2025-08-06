package io.kk.bandhub.venueservice.repository.booking;

import io.kk.bandhub.venueservice.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    Booking save(Booking booking);
    void deleteById(int id);
    Optional<Booking> findById(int id);
    List<Booking> findAll();
    List<Booking> findAllByOfferId(Integer offerId);
    List<Booking> findByTenantIdAndOfferDateGreaterThanEqual(Integer tenantId, LocalDate date);
    Page<Booking> findAllByTenantId(Integer tenantId, Pageable pageable);
    Page<Booking> findAllByOffer_Venue_OwnerId(Integer ownerId, Pageable pageable);
    Page<Booking> findAll(Specification<Booking> spec, Pageable pageable);
}
