package io.kk.bandhub.venueservice.repository.venue;

import io.kk.bandhub.venueservice.model.Venue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface VenueRepository {
    Venue save(Venue venue);
    void deleteById(int id);
    Optional<Venue> findById(int id);
    Optional<Venue> findByNameAndOwnerId(String name, Integer ownerId);
    List<Venue> findAll();
    Page<Venue> findAll(Pageable pageable);
    Page<Venue> findAll(Specification<Venue> spec, Pageable pageable);
}
