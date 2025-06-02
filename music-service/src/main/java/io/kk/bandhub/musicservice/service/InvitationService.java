package io.kk.bandhub.musicservice.service;

import io.kk.bandhub.musicservice.dto.*;
import io.kk.bandhub.musicservice.exception.band.BandError;
import io.kk.bandhub.musicservice.exception.band.BandException;
import io.kk.bandhub.musicservice.exception.band_joining.BandJoiningError;
import io.kk.bandhub.musicservice.exception.band_joining.BandJoiningException;
import io.kk.bandhub.musicservice.exception.musician.MusicianError;
import io.kk.bandhub.musicservice.exception.musician.MusicianException;
import io.kk.bandhub.musicservice.model.BandInvitation;
import io.kk.bandhub.musicservice.model.BandMusician;
import io.kk.bandhub.musicservice.model.RequestStatus;
import io.kk.bandhub.musicservice.repository.band.BandRepository;
import io.kk.bandhub.musicservice.repository.band_invitation.BandInvitationRepository;
import io.kk.bandhub.musicservice.repository.musician.MusicianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InvitationService {
    private final BandRepository bandRepository;
    private final MusicianRepository musicianRepository;
    private final BandInvitationRepository bandInvitationRepository;
    private final AccountServiceClient accountServiceClient;
    private final RabbitService rabbitService;
    private final Mapper mapper;

    public void createInvitation(BandMembershipRequestDTO membershipRequestDTO) {
        var band = bandRepository.findBandById(membershipRequestDTO.getBandId()).orElseThrow(
                () -> new BandException(BandError.BAND_NOT_FOUND)
        );

        var musician = musicianRepository.findMusicianById(membershipRequestDTO.getMusicianId()).orElseThrow(
                () -> new MusicianException(MusicianError.MUSICIAN_NOT_FOUND)
        );

        BandInvitation bandInvitation = new BandInvitation(band, musician, RequestStatus.PENDING, LocalDate.now());
        bandInvitationRepository.save(bandInvitation);

        NotificationRequestDTO dto = new NotificationRequestDTO();
        dto.setType(NotificationType.BAND_INVITATION);
        dto.setRecipientId(membershipRequestDTO.getMusicianId());
        dto.setTitle("Zaproszenie do zespołu");
        dto.setContent("Otrzymałeś/aś nowe zaproszenie do zespołu %s. Odpowiedz na nie jego właścicielowi.".formatted(band.getName()));

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("bandId", band.getId());
        dto.setMetadata(metadata);

        rabbitService.sendNotification(dto);
    }

    public void updateInvitation(BandInvitationDTO invitationDTO) {
        var invitation = bandInvitationRepository.findById(invitationDTO.getId()).orElseThrow(
                () -> new BandJoiningException(BandJoiningError.INVITATION_NOT_FOUND)
        );

        invitation.setStatus(invitationDTO.getStatus());

        var band = bandRepository.findBandById(invitationDTO.getBandId()).orElseThrow(
                () -> new BandException(BandError.BAND_NOT_FOUND)
        );

        if(invitation.getStatus().equals(RequestStatus.ACCEPTED)) {
            var musician = musicianRepository.findMusicianById(invitationDTO.getInvitedMusicianId()).orElseThrow(
                    () -> new MusicianException(MusicianError.MUSICIAN_NOT_FOUND)
            );
            BandMusician bandMusician = new BandMusician(band, musician, LocalDate.now(), false);
            band.addBandMusician(bandMusician);
            bandRepository.save(band);
        }

        var invitations = bandInvitationRepository.findByBand_IdAndMusician_Id(invitationDTO.getBandId(), invitationDTO.getInvitedMusicianId());
        for (BandInvitation inv : invitations) {
            if(!inv.getStatus().equals(RequestStatus.PENDING)) {
                inv.setStatus(RequestStatus.CANCELED);
            }
        }
        bandInvitationRepository.save(invitation);

        if(invitation.getStatus().equals(RequestStatus.ACCEPTED)) {
            NotificationRequestDTO dto = new NotificationRequestDTO();
            dto.setType(NotificationType.JOINED_BAND);
            dto.setRecipientId(bandRepository.getFounderId(invitationDTO.getBandId()));
            dto.setTitle("Zaakceptowano zaproszenie");

            var account = accountServiceClient.getAccount(invitationDTO.getInvitedMusicianId());

            dto.setContent("%s %s zaakceptował zaproszenie do zespołu %s, teraz jest jego członkiem!"
                    .formatted(account.getFirstName(), account.getLastName(), band.getName()));

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("bandId", band.getId());
            dto.setMetadata(metadata);

            rabbitService.sendNotification(dto);
        }
    }

    public List<BandInvitationDTO> getMusicianInvitations(Integer id) {
        return bandInvitationRepository.findByMusician_IdAndStatus(id, RequestStatus.PENDING)
                .stream().map(mapper::toBandInvitationDTO).toList();
    }
}
