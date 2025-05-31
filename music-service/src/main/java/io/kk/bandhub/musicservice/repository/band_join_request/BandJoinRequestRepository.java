package io.kk.bandhub.musicservice.repository.band_join_request;

import io.kk.bandhub.musicservice.model.BandJoinRequest;
import io.kk.bandhub.musicservice.model.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface BandJoinRequestRepository {
    BandJoinRequest save(BandJoinRequest bandJoinRequest);
    Optional<BandJoinRequest> findById(Integer id);
    List<BandJoinRequest> findAll();
    List<BandJoinRequest> findByBand_IdAndStatus(Integer band_id, RequestStatus status);
    List<BandJoinRequest> findByMusician_Id(Integer musician_id);
    List<BandJoinRequest> findByBand_IdAndMusician_Id(Integer bandId, Integer musicianId);
}
