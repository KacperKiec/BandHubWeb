package io.kk.bandhub.bandhubbackend.repository;

import io.kk.bandhub.bandhubbackend.model.Band;
import io.kk.bandhub.bandhubbackend.model.Musician;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Repository interface for managing Musician entities.
 * Provides methods to perform CRUD operations and custom queries on Musician data.
 */
public interface MusicianRepository {
    Musician save(Musician musician);
    List<Musician> findAll();
    Musician findMusicianById(int id);

    List<Musician> findAll(Specification<Musician> specification);

}
