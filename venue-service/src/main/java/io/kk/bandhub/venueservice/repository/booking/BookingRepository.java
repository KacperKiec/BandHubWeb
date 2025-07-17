package io.kk.bandhub.venueservice.repository.booking;

import io.kk.bandhub.venueservice.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    Booking save(Booking booking);
    void deleteById(int id);
    Optional<Booking> findById(int id);
    List<Booking> findAll();
    List<Booking> findAllByOfferId(Integer offerId);
    Page<Booking> findAll(Pageable pageable);
    Page<Booking> findAll(Specification<Booking> spec, Pageable pageable);
}
