package io.kk.bandhub.venueservice.repository.venue;

import io.kk.bandhub.venueservice.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaVenueRepository extends JpaRepository<Venue, Integer>, VenueRepository, JpaSpecificationExecutor<Venue> {
}
