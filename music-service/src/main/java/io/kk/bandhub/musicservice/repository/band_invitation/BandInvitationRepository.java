package io.kk.bandhub.musicservice.repository.band_invitation;

import io.kk.bandhub.musicservice.model.BandInvitation;

import java.util.List;
import java.util.Optional;

public interface BandInvitationRepository {
    BandInvitation save(BandInvitation bandInvitation);
    List<BandInvitation> findAll();
    Optional<BandInvitation> findById(Integer id);
    List<BandInvitation> findByBandId(Integer bandId);
    List<BandInvitation> findByMusician_Id(Integer musicianId);
    List<BandInvitation> findByBand_IdAndMusician_Id(Integer bandId, Integer musicianId);
}
