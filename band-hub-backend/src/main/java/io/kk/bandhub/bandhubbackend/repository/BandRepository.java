package io.kk.bandhub.bandhubbackend.repository;

import io.kk.bandhub.bandhubbackend.model.Band;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Band entities.
 * Provides methods to perform CRUD operations and custom queries on Band data.
 */
public interface BandRepository {
    Band save(Band band);
    Optional<Band> findBandById(int id);
    Page<Band> findAll(Pageable pageable);
    Page<Band> findAll(Specification<Band> spec, Pageable pageable);
}
