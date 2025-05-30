package io.kk.bandhub.musicservice.repository.band_join_request;

import io.kk.bandhub.musicservice.model.BandJoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaBandJoinRequestRepository extends JpaRepository<BandJoinRequest, Integer>, BandJoinRequestRepository {

}
