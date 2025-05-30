package io.kk.bandhub.musicservice.repository.band_invitation;

import io.kk.bandhub.musicservice.model.BandInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaBandInvitationRepository extends JpaRepository<BandInvitation, Integer>, BandInvitationRepository {


}
