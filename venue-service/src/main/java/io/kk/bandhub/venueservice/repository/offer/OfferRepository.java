package io.kk.bandhub.venueservice.repository.offer;

import io.kk.bandhub.venueservice.model.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface OfferRepository {
    Offer save(Offer offer);
    void deleteById(int id);
    Optional<Offer> findById(int id);
    List<Offer> findAll();
    List<Offer> findByVenueOwnerId(Integer venueOwnerId);
    Page<Offer> findAll(Pageable pageable);
    Page<Offer> findAll(Specification<Offer> spec, Pageable pageable);
}
