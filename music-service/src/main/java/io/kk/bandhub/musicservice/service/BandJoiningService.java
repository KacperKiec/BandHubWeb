package io.kk.bandhub.musicservice.service;

import io.kk.bandhub.musicservice.dto.BandInvitationDTO;
import io.kk.bandhub.musicservice.dto.BandJoinRequestDTO;
import io.kk.bandhub.musicservice.dto.BandMembershipRequestDTO;
import io.kk.bandhub.musicservice.dto.Mapper;
import io.kk.bandhub.musicservice.exception.band.BandError;
import io.kk.bandhub.musicservice.exception.band.BandException;
import io.kk.bandhub.musicservice.exception.band_joining.BandJoiningError;
import io.kk.bandhub.musicservice.exception.band_joining.BandJoiningException;
import io.kk.bandhub.musicservice.exception.musician.MusicianError;
import io.kk.bandhub.musicservice.exception.musician.MusicianException;
import io.kk.bandhub.musicservice.model.BandInvitation;
import io.kk.bandhub.musicservice.model.BandJoinRequest;
import io.kk.bandhub.musicservice.model.BandMusician;
import io.kk.bandhub.musicservice.model.RequestStatus;
import io.kk.bandhub.musicservice.repository.band.BandRepository;
import io.kk.bandhub.musicservice.repository.band_invitation.BandInvitationRepository;
import io.kk.bandhub.musicservice.repository.band_join_request.BandJoinRequestRepository;
import io.kk.bandhub.musicservice.repository.musician.MusicianRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class BandJoiningService {
    private final BandRepository bandRepository;
    private final MusicianRepository musicianRepository;
    private final BandInvitationRepository bandInvitationRepository;
    private final BandJoinRequestRepository bandJoinRequestRepository;
    private final Mapper mapper;

    public void createInvitation(BandMembershipRequestDTO invitationDTO) {
        var band = bandRepository.findBandById(invitationDTO.getBandId()).orElseThrow(
                () -> new BandException(BandError.BAND_NOT_FOUND)
        );

        var musician = musicianRepository.findMusicianById(invitationDTO.getMusicianId()).orElseThrow(
                () -> new MusicianException(MusicianError.MUSICIAN_NOT_FOUND)
        );

        BandInvitation bandInvitation = new BandInvitation(band, musician, RequestStatus.PENDING, LocalDate.now());
        bandInvitationRepository.save(bandInvitation);
    }

    public void createJoinRequest(BandMembershipRequestDTO joinRequestDTO) {
        var band = bandRepository.findBandById(joinRequestDTO.getBandId()).orElseThrow(
                () -> new BandException(BandError.BAND_NOT_FOUND)
        );

        var musician = musicianRepository.findMusicianById(joinRequestDTO.getMusicianId()).orElseThrow(
                () -> new MusicianException(MusicianError.MUSICIAN_NOT_FOUND)
        );

        BandJoinRequest bandJoinRequest = new BandJoinRequest(band, musician, RequestStatus.PENDING, LocalDate.now());
        bandJoinRequestRepository.save(bandJoinRequest);
    }

    public void updateInvitation(BandInvitationDTO invitationDTO) {
        var invitation = bandInvitationRepository.findById(invitationDTO.getId()).orElseThrow(
                () -> new BandJoiningException(BandJoiningError.INVITATION_NOT_FOUND)
        );

        invitation.setStatus(invitationDTO.getStatus());

        if(invitation.getStatus().equals(RequestStatus.ACCEPTED)) {
            var band = bandRepository.findBandById(invitationDTO.getBandId()).orElseThrow(
                    () -> new BandException(BandError.BAND_NOT_FOUND)
            );
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
    }

    public void updateRequest(BandJoinRequestDTO requestDTO) {
        var request = bandJoinRequestRepository.findById(requestDTO.getId()).orElseThrow(
                () -> new BandJoiningException(BandJoiningError.JOINING_REQUEST_NOT_FOUND)
        );

        request.setStatus(requestDTO.getStatus());

        if(requestDTO.getStatus().equals(RequestStatus.ACCEPTED)) {
            var band = bandRepository.findBandById(requestDTO.getBandId()).orElseThrow(
                    () -> new BandException(BandError.BAND_NOT_FOUND)
            );
            var musician = musicianRepository.findMusicianById(requestDTO.getApplicantId()).orElseThrow(
                    () -> new MusicianException(MusicianError.MUSICIAN_NOT_FOUND)
            );
            BandMusician bandMusician = new BandMusician(band, musician, LocalDate.now(), false);
            band.addBandMusician(bandMusician);
            bandRepository.save(band);
        }

        var requests = bandJoinRequestRepository.findByBand_IdAndMusician_Id(requestDTO.getBandId(), requestDTO.getApplicantId());
        for (BandJoinRequest req : requests) {
            if(!req.getStatus().equals(RequestStatus.PENDING)) {
                req.setStatus(RequestStatus.CANCELED);
            }
        }
        bandJoinRequestRepository.save(request);
    }

    public List<BandInvitationDTO> getMusicianInvitations(Integer id) {
        return bandInvitationRepository.findByMusician_IdAndStatus(id, RequestStatus.PENDING)
                .stream().map(mapper::toBandInvitationDTO).toList();
    }

    public List<BandJoinRequestDTO> getBandJoinRequests(Integer id) {
        return bandJoinRequestRepository.findByBand_IdAndStatus(id, RequestStatus.PENDING)
                .stream().map(mapper::toBandRequestDTO).toList();
    }
}
