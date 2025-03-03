package io.kk.bandhub.musicservice.service;

import io.kk.bandhub.musicservice.dto.*;
import io.kk.bandhub.musicservice.exception.band.BandError;
import io.kk.bandhub.musicservice.exception.band.BandException;
import io.kk.bandhub.musicservice.exception.genre.GenreError;
import io.kk.bandhub.musicservice.exception.genre.GenreException;
import io.kk.bandhub.musicservice.exception.musician.MusicianError;
import io.kk.bandhub.musicservice.exception.musician.MusicianException;
import io.kk.bandhub.musicservice.model.*;
import io.kk.bandhub.musicservice.repository.band.BandRepository;
import io.kk.bandhub.musicservice.repository.band.BandSpecification;
import io.kk.bandhub.musicservice.repository.genre.GenreRepository;
import io.kk.bandhub.musicservice.repository.musician.MusicianRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class for provide data and method for BandController
 * */
@Service
public class BandService {
    private final BandRepository bandRepository;
    private final MusicianRepository musicianRepository;
    private final GenreRepository genreRepository;
    private final Mapper mapper;

    public BandService(final BandRepository bandRepository, MusicianRepository musicianRepository, GenreRepository genreRepository, Mapper mapper) {
        this.bandRepository = bandRepository;
        this.musicianRepository = musicianRepository;
        this.genreRepository = genreRepository;
        this.mapper = mapper;
    }

    @Transactional
    public BandDTO save(BandDTO bandDTO) {

        Musician founder = musicianRepository.findMusicianById(bandDTO.getFounderId())
                .orElseThrow(() ->new MusicianException(MusicianError.MUSICIAN_NOT_FOUND));

        Band band = new Band();
        band.setName(bandDTO.getName());
        band.setBio(bandDTO.getBio());
        band.setCity(bandDTO.getCity());
        band.setIsLookingMembers(bandDTO.isLookingMembers());

        for(GenreDTO genre : bandDTO.getGenres()) {
            var exitingGenre = genreRepository.findById(genre.getId()).orElseThrow(
                    () -> new GenreException(GenreError.GENRE_NOT_FOUND)
            );
            BandGenre bandGenre = new BandGenre(band, exitingGenre);
            band.addBandGenre(bandGenre);
        }

        for(MusicianDTO musicianDTO : bandDTO.getMusicians()) {
            var musician = musicianRepository.findMusicianById(musicianDTO.getId()).orElseThrow(
                    () -> new MusicianException(MusicianError.MUSICIAN_NOT_FOUND)
            );
            boolean isFounder = musicianDTO.getId().equals(bandDTO.getFounderId());
            BandMusician bandMusician = new BandMusician(band, musician, LocalDate.now(), isFounder);
            band.addBandMusician(bandMusician);
        }

        var savedBand = bandRepository.save(band);

        return mapper.toBandDTO(savedBand);
    }

    public BandDTO getById(int id) {
        var band = bandRepository.findBandById(id)
                .orElseThrow(() -> new BandException(BandError.BAND_NOT_FOUND));
        return mapper.toBandDTO(band);
    }

    public BandEvtDTO getBandEvtById(int id) {
        var band = bandRepository.findBandById(id).orElseThrow(
                () -> new BandException(BandError.BAND_NOT_FOUND)
        );
        return mapper.toBandEvtDTO(band);
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
    public BandDTO updateBand(BandDTO bandDTO) {
        var band = bandRepository.findBandById(bandDTO.getId()).orElseThrow(
                () ->new BandException(BandError.BAND_NOT_FOUND));

        band.setName(bandDTO.getName());
        band.setIsLookingMembers(bandDTO.isLookingMembers());
        band.setCity(bandDTO.getCity());
        band.setBio(bandDTO.getBio());

        for(GenreDTO genre : bandDTO.getGenres()) {
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

        for (BandGenre bandGenre : band.getBandGenres()) {
            boolean genreExistsInDTO = bandDTO.getGenres().stream()
                    .anyMatch(genre -> bandGenre.getGenre().getId().equals(genre.getId()));

            if (!genreExistsInDTO) {
                band.removeBandGenre(bandGenre);
            }
        }

        for (MusicianDTO musicianDTO : bandDTO.getMusicians()) {
            boolean musicianExistsInBand = band.getBandMusicians().stream()
                    .anyMatch(bandMusician -> bandMusician.getMusician().getId().equals(musicianDTO.getId()));

            if (!musicianExistsInBand) {
                var musician = musicianRepository.findMusicianById(musicianDTO.getId()).orElseThrow(
                        () -> new MusicianException(MusicianError.MUSICIAN_NOT_FOUND)
                );

                BandMusician newBandMusician = new BandMusician(band, musician, LocalDate.now(), false);
                band.addBandMusician(newBandMusician);
            }
        }

        for (BandMusician bandMusician : band.getBandMusicians()) {
            boolean musicianExistsInDTO = bandDTO.getMusicians().stream()
                    .anyMatch(musicianDTO -> bandMusician.getMusician().getId().equals(musicianDTO.getId()));

            if (!musicianExistsInDTO) {
                band.removeBandMusician(bandMusician);
            }
        }

        return mapper.toBandDTO(bandRepository.save(band));
    }

    @Transactional
    public void deleteBand(int bandId) {
        var band = bandRepository.findBandById(bandId).orElseThrow(
                () -> new BandException(BandError.BAND_NOT_FOUND));
        bandRepository.deleteBandById(bandId);
    }

}
