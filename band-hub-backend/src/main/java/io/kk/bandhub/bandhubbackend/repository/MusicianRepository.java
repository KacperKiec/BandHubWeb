package io.kk.bandhub.bandhubbackend.repository;

import io.kk.bandhub.bandhubbackend.model.Band;
import io.kk.bandhub.bandhubbackend.model.Musician;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Musician entities.
 * Provides methods to perform CRUD operations and custom queries on Musician data.
 */
public interface MusicianRepository {
    Musician save(Musician musician);
    Optional<Musician> findMusicianById(int id);
    Page<Musician> findAll(Pageable pageable);
    Page<Musician> findAll(Specification<Musician> specification, Pageable pageable);

}
