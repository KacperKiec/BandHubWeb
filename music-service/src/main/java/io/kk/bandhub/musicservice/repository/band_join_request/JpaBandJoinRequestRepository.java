package io.kk.bandhub.musicservice.repository.band_join_request;

import io.kk.bandhub.musicservice.model.BandJoinRequest;
import io.kk.bandhub.musicservice.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaBandJoinRequestRepository extends JpaRepository<BandJoinRequest, Integer>, BandJoinRequestRepository {

}
