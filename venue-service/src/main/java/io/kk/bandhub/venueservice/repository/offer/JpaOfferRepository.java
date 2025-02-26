package io.kk.bandhub.venueservice.repository.offer;

import io.kk.bandhub.venueservice.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOfferRepository extends JpaRepository<Offer, Integer>, OfferRepository,  JpaSpecificationExecutor<Offer> {

}
