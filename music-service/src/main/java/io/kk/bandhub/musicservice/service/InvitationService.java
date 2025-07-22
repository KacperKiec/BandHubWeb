package io.kk.bandhub.musicservice.service;

import io.kk.bandhub.musicservice.dto.BandInvitationDTO;
import io.kk.bandhub.musicservice.dto.BandInvitationUpdateRequestDTO;
import io.kk.bandhub.musicservice.dto.BandMembershipRequestDTO;

import java.util.List;

public interface InvitationService {
    void createInvitation(BandMembershipRequestDTO membershipRequestDTO);
    void updateInvitation(BandInvitationUpdateRequestDTO invitationDTO);
    List<BandInvitationDTO> getMusicianInvitations(Integer id);
}
