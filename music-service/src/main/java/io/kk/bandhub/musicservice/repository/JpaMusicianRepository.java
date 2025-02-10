package io.kk.bandhub.musicservice.repository;

import io.kk.bandhub.musicservice.model.Musician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * JPA repository interface for managing Musician entities.
 * Extends JpaRepository to provide basic CRUD operations,
 * JpaSpecificationExecutor for more complex queries and
 * inherits methods from the MusicianRepository interface.
 */
@Repository
interface JpaMusicianRepository extends JpaRepository<Musician, Integer>, MusicianRepository, JpaSpecificationExecutor<Musician> {

}
