package io.kk.bandhub.musicservice.repository;

import io.kk.bandhub.musicservice.model.Band;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * JPA repository interface for managing Band entities.
 * Extends JpaRepository to provide basic CRUD operations and
 * inherits methods from the BandRepository interface.
 */
@Repository
interface JpaBandRepository extends JpaRepository<Band, Integer>, BandRepository, JpaSpecificationExecutor<Band> {
}
