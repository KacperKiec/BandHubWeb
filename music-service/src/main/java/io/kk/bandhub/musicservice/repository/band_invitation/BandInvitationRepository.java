package io.kk.bandhub.musicservice.repository.band_invitation;

import io.kk.bandhub.musicservice.model.BandInvitation;
import io.kk.bandhub.musicservice.model.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface BandInvitationRepository {
    BandInvitation save(BandInvitation bandInvitation);
    List<BandInvitation> findAll();
    Optional<BandInvitation> findById(Integer id);
    List<BandInvitation> findByBandId(Integer bandId);
    List<BandInvitation> findByMusician_IdAndStatus(Integer musicianId, RequestStatus status);
    List<BandInvitation> findByBand_IdAndMusician_Id(Integer bandId, Integer musicianId);
}
