package io.kk.bandhub.musicservice.service.impl;

import io.kk.bandhub.musicservice.dto.*;
import io.kk.bandhub.musicservice.exception.band.BandError;
import io.kk.bandhub.musicservice.exception.band.BandException;
import io.kk.bandhub.musicservice.exception.band_joining.BandJoiningError;
import io.kk.bandhub.musicservice.exception.band_joining.BandJoiningException;
import io.kk.bandhub.musicservice.exception.musician.MusicianError;
import io.kk.bandhub.musicservice.exception.musician.MusicianException;
import io.kk.bandhub.musicservice.integration.RabbitService;
import io.kk.bandhub.musicservice.model.BandJoinRequest;
import io.kk.bandhub.musicservice.model.BandMusician;
import io.kk.bandhub.musicservice.model.RequestStatus;
import io.kk.bandhub.musicservice.repository.band.BandRepository;
import io.kk.bandhub.musicservice.repository.band_join_request.BandJoinRequestRepository;
import io.kk.bandhub.musicservice.repository.musician.MusicianRepository;
import io.kk.bandhub.musicservice.service.RequestService;
import io.kk.bandhub.musicservice.utils.NotificationCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final BandRepository bandRepository;
    private final MusicianRepository musicianRepository;
    private final BandJoinRequestRepository bandJoinRequestRepository;
    private final RabbitService rabbitService;
    private final Mapper mapper;

    public void createJoinRequest(BandMembershipRequestDTO joinRequestDTO) {
        var band = bandRepository.findBandById(joinRequestDTO.getBandId()).orElseThrow(
                () -> new BandException(BandError.BAND_NOT_FOUND)
        );

        var musician = musicianRepository.findMusicianById(joinRequestDTO.getMusicianId()).orElseThrow(
                () -> new MusicianException(MusicianError.MUSICIAN_NOT_FOUND)
        );

        BandJoinRequest bandJoinRequest = new BandJoinRequest(band, musician, RequestStatus.PENDING, LocalDate.now());
        bandJoinRequestRepository.save(bandJoinRequest);

        String title = "Zgłoszenie do zespołu";
        String content = "Otrzymałeś/aś nowe zgłoszenie do zespołu %s. Odpowiedz na nie zainteresowanemu muzykowi.".formatted(band.getName());

        var dto = NotificationCreator.createNotification(NotificationType.BAND_REQUEST, bandRepository.getFounderId(band.getId()), title, content);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("bandId", band.getId());
        metadata.put("userId", musician.getId());
        dto.setMetadata(metadata);

        rabbitService.sendNotification(dto);
    }

    public void updateRequest(BandJoinUpdateRequestDTO requestDTO) {
        var request = bandJoinRequestRepository.findById(requestDTO.getId()).orElseThrow(
                () -> new BandJoiningException(BandJoiningError.JOINING_REQUEST_NOT_FOUND)
        );

        request.setStatus(requestDTO.getStatus());

        var band = bandRepository.findBandById(requestDTO.getBandId()).orElseThrow(
                () -> new BandException(BandError.BAND_NOT_FOUND)
        );

        if(requestDTO.getStatus().equals(RequestStatus.ACCEPTED)) {
            var musician = musicianRepository.findMusicianById(requestDTO.getApplicantId()).orElseThrow(
                    () -> new MusicianException(MusicianError.MUSICIAN_NOT_FOUND)
            );
            BandMusician bandMusician = new BandMusician(band, musician, LocalDate.now(), false);
            band.addBandMusician(bandMusician);
            bandRepository.save(band);
        }
        bandJoinRequestRepository.save(request);

        var requests = bandJoinRequestRepository.findByBand_IdAndMusician_Id(requestDTO.getBandId(), requestDTO.getApplicantId());
        for (BandJoinRequest req : requests) {
            if(req.getStatus().equals(RequestStatus.PENDING)) {
                req.setStatus(RequestStatus.CANCELED);
            }
            bandJoinRequestRepository.save(req);
        }

        if(requestDTO.getStatus().equals(RequestStatus.ACCEPTED)) {

            String title = "Zaakceptowano zgłoszenie";
            String content = "Właściciel zespołu %s zaakceptował twoje zgłoszenie, teraz jesteś jego częścią!"
                    .formatted(band.getName());

            var dto = NotificationCreator.createNotification(NotificationType.JOINED_BAND, requestDTO.getApplicantId(), title, content);

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("bandId", band.getId());
            dto.setMetadata(metadata);

            rabbitService.sendNotification(dto);
        } else if (requestDTO.getStatus().equals(RequestStatus.REJECTED)) {

            String title = "Odrzucono zgłoszenie";
            String content = "Właściciel zespołu %s odrzucił twoje zgłoszenie, spróbuj swoich sił gdzie indziej."
                    .formatted(band.getName());

            var dto = NotificationCreator.createNotification(NotificationType.BAND_REQUEST_REJECTED, requestDTO.getApplicantId(), title, content);

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("bandId", band.getId());
            dto.setMetadata(metadata);

            rabbitService.sendNotification(dto);
        }
    }

    public List<BandJoinRequestDTO> getBandJoinRequests(Integer id) {
        return bandJoinRequestRepository.findByBand_IdAndStatus(id, RequestStatus.PENDING)
                .stream().map(mapper::toBandRequestDTO).toList();
    }
}
