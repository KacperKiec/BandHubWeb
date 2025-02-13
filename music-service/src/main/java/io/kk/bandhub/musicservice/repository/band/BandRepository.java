package io.kk.bandhub.musicservice.repository.band;


import io.kk.bandhub.musicservice.model.Band;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

/**
 * Repository interface for managing Band entities.
 * Provides methods to perform CRUD operations and custom queries on Band data.
 */
public interface BandRepository {
    Band save(Band band);
    void deleteBandById(Integer id);
    Optional<Band> findBandById(Integer id);
    Page<Band> findAll(Pageable pageable);
    Page<Band> findAll(Specification<Band> spec, Pageable pageable);
}
