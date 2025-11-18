package io.kk.bandhub.venueservice.repository.offer;

import io.kk.bandhub.venueservice.model.Offer;
import io.kk.bandhub.venueservice.utils.OfferStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OfferRepository {
    Offer save(Offer offer);
    void deleteById(int id);
    void deleteAll();
    Optional<Offer> findById(int id);
    List<Offer> findAll();
    List<Offer> findByVenueOwnerId(Integer venueOwnerId);
    List<Offer> findOffersByDateAndVenueId(LocalDate date, Integer venueId);
    List<Offer> findOffersByDateAndVenueIdAndStatusIn(LocalDate date, Integer venueId, List<OfferStatus> status);
    List<Offer> findOffersByVenueId(Integer venueId);
    Page<Offer> findAll(Pageable pageable);
    List<Offer> findAll(Specification<Offer> spec);
}
