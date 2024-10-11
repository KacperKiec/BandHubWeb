package io.kk.bandhub.bandhubbackend.repository;

import io.kk.bandhub.bandhubbackend.model.Band;
import io.kk.bandhub.bandhubbackend.model.Musician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA repository interface for managing Musician entities.
 * Extends JpaRepository to provide basic CRUD operations,
 * JpaSpecificationExecutor for more complex queries and
 * inherits methods from the MusicianRepository interface.
 */
@Repository
interface JpaMusicianRepository extends JpaRepository<Musician, Integer>, MusicianRepository, JpaSpecificationExecutor<Musician> {

}
