package io.kk.bandhub.bandhubbackend.repository;

import io.kk.bandhub.bandhubbackend.model.Band;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Repository interface for managing Band entities.
 * Provides methods to perform CRUD operations and custom queries on Band data.
 */
public interface BandRepository {
    Band save(Band band);
    Band findBandById(int id);
    List<Band> findAll();

    List<Band> findAll(Specification<Band> spec);
}
