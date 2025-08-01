package io.kk.bandhub.musicservice.service.impl;

import io.kk.bandhub.musicservice.dto.*;
import io.kk.bandhub.musicservice.exception.band.BandError;
import io.kk.bandhub.musicservice.exception.band.BandException;
import io.kk.bandhub.musicservice.exception.genre.GenreError;
import io.kk.bandhub.musicservice.exception.genre.GenreException;
import io.kk.bandhub.musicservice.exception.musician.MusicianError;
import io.kk.bandhub.musicservice.exception.musician.MusicianException;
import io.kk.bandhub.musicservice.integration.AccountServiceClient;
import io.kk.bandhub.musicservice.integration.RabbitService;
import io.kk.bandhub.musicservice.model.Band;
import io.kk.bandhub.musicservice.model.BandGenre;
import io.kk.bandhub.musicservice.model.BandMusician;
import io.kk.bandhub.musicservice.model.Musician;
import io.kk.bandhub.musicservice.repository.band.BandRepository;
import io.kk.bandhub.musicservice.repository.band.BandSpecification;
import io.kk.bandhub.musicservice.repository.genre.GenreRepository;
import io.kk.bandhub.musicservice.repository.musician.MusicianRepository;
import io.kk.bandhub.musicservice.service.BandService;
import io.kk.bandhub.musicservice.utils.NotificationCreator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Service class for provide data and method for BandController
 * */
@Service
@RequiredArgsConstructor
public class BandServiceImpl implements BandService {
    private final BandRepository bandRepository;
    private final MusicianRepository musicianRepository;
    private final GenreRepository genreRepository;
    private final RabbitService rabbitService;
    private final AccountServiceClient accountServiceClient;
    private final Mapper mapper;

    @Transactional
    public BandDTO save(BandRequestDTO bandDTO) {

        Musician founder = musicianRepository.findMusicianById(bandDTO.getFounderId())
                .orElseThrow(() ->new MusicianException(MusicianError.MUSICIAN_NOT_FOUND));

        Band band = new Band();
        band.setName(bandDTO.getName());
        band.setBio(bandDTO.getBio());
        band.setCity(bandDTO.getCity());
        band.setCountry(bandDTO.getCountry());
        band.setIsLookingMembers(bandDTO.isLookingMembers());

        for(GenreDTO genre : bandDTO.getGenres()) {
            var exitingGenre = genreRepository.findById(genre.getId()).orElseThrow(
                    () -> new GenreException(GenreError.GENRE_NOT_FOUND)
            );
            BandGenre bandGenre = new BandGenre(band, exitingGenre);
            band.addBandGenre(bandGenre);
        }

        BandMusician bandMusician = new BandMusician(band, founder, LocalDate.now(), true);
        band.addBandMusician(bandMusician);

        var savedBand = bandRepository.save(band);

        return mapper.toBandDTO(savedBand);
    }

    public BandDTO getById(Integer id) {
        var band = bandRepository.findBandById(id)
                .orElseThrow(() -> new BandException(BandError.BAND_NOT_FOUND));
        return mapper.toBandDTO(band);
    }

    public BandEventDataDTO getBandEvtById(Integer id) {
        var band = bandRepository.findBandById(id).orElseThrow(
                () -> new BandException(BandError.BAND_NOT_FOUND)
        );

        var founderId = bandRepository.getFounderId(id);
        return mapper.toBandEvtDTO(band, founderId);
    }

    public Page<BandDTO> getAll(Pageable pageable) {
        return bandRepository.findAll(pageable).map(mapper::toBandDTO);
    }

    public Page<BandDTO> findBandByFilters(
            String name,
            List<String> cities,
            Integer founderId,
            List<String> genres,
            Boolean isLookingMembers,
            Pageable pageable) {

        Specification<Band> spec = Specification.where(BandSpecification.hasName(name)
                .and(BandSpecification.hasCity(cities))
                .and(BandSpecification.hasFounderId(founderId))
                .and(BandSpecification.hasGenres(genres))
                .and(BandSpecification.isLookingMembers(isLookingMembers)));
        return bandRepository.findAll(spec, pageable).map(mapper::toBandDTO);
    }

    @Transactional
    public BandDTO updateBand(BandUpdateRequestDTO bandDTO) {
        var band = bandRepository.findBandById(bandDTO.getId()).orElseThrow(
                () ->new BandException(BandError.BAND_NOT_FOUND));

        band.setName(bandDTO.getName());
        band.setIsLookingMembers(bandDTO.isLookingMembers());
        band.setCity(bandDTO.getCity());
        band.setCountry(bandDTO.getCountry());
        band.setBio(bandDTO.getBio());

        for(GenreDTO genre : new HashSet<>(bandDTO.getGenres())) {
            var g = genreRepository.findById(genre.getId()).orElseThrow(
                    () -> new GenreException(GenreError.GENRE_NOT_FOUND)
            );
            boolean genreExistsInBand = band.getBandGenres().stream()
                    .anyMatch(bandGenre -> bandGenre.getGenre().equals(g));

            if (!genreExistsInBand) {
                BandGenre newBandGenre = new BandGenre(band, g);
                band.addBandGenre(newBandGenre);
            }
        }

        for (BandGenre bandGenre : new HashSet<>(band.getBandGenres())) {
            boolean genreExistsInDTO = bandDTO.getGenres().stream()
                    .anyMatch(genre -> bandGenre.getGenre().getId().equals(genre.getId()));

            if (!genreExistsInDTO) {
                band.removeBandGenre(bandGenre);
            }
        }

        return mapper.toBandDTO(bandRepository.save(band));
    }

    public void addMusicianToBand(BandMembershipRequestDTO dto) {
        var band = bandRepository.findBandById(dto.getBandId()).orElseThrow(
                () -> new BandException(BandError.BAND_NOT_FOUND)
        );

        var musician = musicianRepository.findMusicianById(dto.getMusicianId()).orElseThrow(
                () -> new MusicianException(MusicianError.MUSICIAN_NOT_FOUND)
        );

        BandMusician bandMusician = new BandMusician(band, musician, LocalDate.now(), false);
        band.addBandMusician(bandMusician);
        bandRepository.save(band);
    }

    public void removeMusicianFromBand(BandMembershipRequestDTO dto) {
        var band = removeMusician(dto);

        String title = "Opuszczono zespół";
        String content = "Właściciel zespołu %s usunął Cię z niego. Nie jesteś już jego członkiem.".formatted(band.getName());

        var notificationRequestDTO = NotificationCreator.createNotification(NotificationType.LEFT_BAND, dto.getMusicianId(), title, content);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("bandId", band.getId());
        notificationRequestDTO.setMetadata(metadata);

        rabbitService.sendNotification(notificationRequestDTO);
    }

    public void leaveBand(BandMembershipRequestDTO dto) {
        var band = removeMusician(dto);
        var account = accountServiceClient.getAccount(dto.getMusicianId());

        String title = "Opuszczono zespół";
        String content = "Użytkownik %s %s opuścił zespół %s".formatted(account.getFirstName(), account.getLastName(), band.getName());

        var notificationRequestDTO = NotificationCreator.createNotification(NotificationType.LEFT_BAND, bandRepository.getFounderId(band.getId()), title, content);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("bandId", band.getId());
        notificationRequestDTO.setMetadata(metadata);

        rabbitService.sendNotification(notificationRequestDTO);
    }

    private Band removeMusician(BandMembershipRequestDTO dto) {
        var band = bandRepository.findBandById(dto.getBandId()).orElseThrow(
                () -> new BandException(BandError.BAND_NOT_FOUND)
        );

        for(BandMusician bm : new HashSet<>(band.getBandMusicians())) {
            if (bm.getMusician().getId().equals(dto.getMusicianId())) {
                band.removeBandMusician(bm);
            }
        }
        bandRepository.save(band);
        return band;
    }

    @Transactional
    public void deleteBand(Integer bandId) {
        var existingBand = bandRepository.findBandById(bandId).orElseThrow(
                () -> new BandException(BandError.BAND_NOT_FOUND));
        bandRepository.deleteBandById(bandId);

        String title = "Rozwiązano zespół";
        String content = "Zespół %s został rozwiązany przez jego właściciela.".formatted(existingBand.getName());
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("bandId", existingBand.getId());

        for (var bm : existingBand.getBandMusicians()) {
            var dto = NotificationCreator.createNotification(NotificationType.DELETED_BAND, bm.getMusician().getId(), title, content);
            dto.setMetadata(metadata);

            rabbitService.sendNotification(dto);
        }
    }

}
