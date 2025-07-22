package io.kk.bandhub.musicservice.service.impl;

import io.kk.bandhub.musicservice.dto.*;
import io.kk.bandhub.musicservice.exception.band.BandError;
import io.kk.bandhub.musicservice.exception.band.BandException;
import io.kk.bandhub.musicservice.exception.band_joining.BandJoiningError;
import io.kk.bandhub.musicservice.exception.band_joining.BandJoiningException;
import io.kk.bandhub.musicservice.exception.musician.MusicianError;
import io.kk.bandhub.musicservice.exception.musician.MusicianException;
import io.kk.bandhub.musicservice.integration.AccountServiceClient;
import io.kk.bandhub.musicservice.integration.RabbitService;
import io.kk.bandhub.musicservice.model.BandInvitation;
import io.kk.bandhub.musicservice.model.BandMusician;
import io.kk.bandhub.musicservice.model.RequestStatus;
import io.kk.bandhub.musicservice.repository.band.BandRepository;
import io.kk.bandhub.musicservice.repository.band_invitation.BandInvitationRepository;
import io.kk.bandhub.musicservice.repository.musician.MusicianRepository;
import io.kk.bandhub.musicservice.service.InvitationService;
import io.kk.bandhub.musicservice.utils.NotificationCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {
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

        String title = "Zaproszenie do zespołu";
        String content = "Otrzymałeś/aś nowe zaproszenie do zespołu %s. Odpowiedz na nie jego właścicielowi.".formatted(band.getName());

        var dto = NotificationCreator.createNotification(NotificationType.BAND_INVITATION, membershipRequestDTO.getMusicianId(), title, content);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("bandId", band.getId());
        dto.setMetadata(metadata);

        rabbitService.sendNotification(dto);
    }

    public void updateInvitation(BandInvitationUpdateRequestDTO invitationDTO) {
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
        bandInvitationRepository.save(invitation);

        var invitations = bandInvitationRepository.findByBand_IdAndMusician_Id(invitationDTO.getBandId(), invitationDTO.getInvitedMusicianId());
        for (BandInvitation inv : invitations) {
            if(inv.getStatus().equals(RequestStatus.PENDING)) {
                inv.setStatus(RequestStatus.CANCELED);
            }
            bandInvitationRepository.save(inv);
        }

        if(invitation.getStatus().equals(RequestStatus.ACCEPTED)) {

            var account = accountServiceClient.getAccount(invitationDTO.getInvitedMusicianId());

            String title = "Zaakceptowano zaproszenie";
            String content = "%s %s zaakceptował zaproszenie do zespołu %s, teraz jest jego członkiem!"
                    .formatted(account.getFirstName(), account.getLastName(), band.getName());

            var dto = NotificationCreator.createNotification(NotificationType.JOINED_BAND, bandRepository.getFounderId(invitationDTO.getBandId()), title, content);

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
