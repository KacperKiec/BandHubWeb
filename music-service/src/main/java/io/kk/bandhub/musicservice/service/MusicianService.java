package io.kk.bandhub.musicservice.service;

import io.kk.bandhub.musicservice.dto.Mapper;
import io.kk.bandhub.musicservice.dto.MusicianDTO;
import io.kk.bandhub.musicservice.exception.MusicianError;
import io.kk.bandhub.musicservice.exception.MusicianException;
import io.kk.bandhub.musicservice.model.*;
import io.kk.bandhub.musicservice.repository.MusicianRepository;
import io.kk.bandhub.musicservice.repository.MusicianSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for provide data and methods for MusicianController
 * */
@Service
public class MusicianService {
    private final MusicianRepository musicianRepository;
    private final Mapper mapper;

    public MusicianService(MusicianRepository musicianRepository, Mapper mapper) {
        this.musicianRepository = musicianRepository;
        this.mapper = mapper;
    }

    @Transactional
    public MusicianDTO save(MusicianDTO musicianDTO) {

        var existingMusician = musicianRepository.findMusicianById(musicianDTO.getId());
        if(existingMusician.isPresent()) {
            throw new MusicianException(MusicianError.MUSICIAN_ALREADY_EXISTS);
        }

        Musician musician = new Musician();
        musician.setId(musicianDTO.getId());
        musician.setBio(musicianDTO.getBio());
        musician.setIsLookingBand(musicianDTO.isLookingBand());

        if(musicianDTO.getGenres() != null) {
            for (Genre genre : musicianDTO.getGenres()) {
                MusicianGenre musicianGenre = new MusicianGenre(musician, genre);
                musician.addMusicianGenre(musicianGenre);
            }
        }

        if(musicianDTO.getInstruments() != null) {
            for (Instrument instrument : musicianDTO.getInstruments()) {
                MusicianInstrument musicianInstrument = new MusicianInstrument(musician, instrument);
                musician.addMusicianInstrument(musicianInstrument);
            }
        }

        var mappedMusician = mapper.toMusicianDTO(musician);
        musicianRepository.save(musician);

        return mappedMusician;
    }

    @Transactional
    public MusicianDTO updateMusician(MusicianDTO musicianDTO) {
        var musician = musicianRepository.findMusicianById(musicianDTO.getId()).orElseThrow(
                () ->new MusicianException(MusicianError.MUSICIAN_NOT_FOUND));

        musician.setId(musicianDTO.getId());
        musician.setBio(musicianDTO.getBio());
        musician.setIsLookingBand(musicianDTO.isLookingBand());

        for (Genre genre : musicianDTO.getGenres()) {
            boolean genreExistsInMusician = musician.getMusicianGenres().stream()
                    .anyMatch(musicianGenre -> musicianGenre.getGenre().equals(genre));

            if (!genreExistsInMusician) {
                MusicianGenre newMusicianGenre = new MusicianGenre(musician, genre);
                musician.addMusicianGenre(newMusicianGenre);
            }
        }

        for (MusicianGenre musicianGenre : musician.getMusicianGenres()) {
            boolean genreExistsInDTO = musicianDTO.getGenres().stream()
                    .anyMatch(genre -> musicianGenre.getGenre().equals(genre));

            if (!genreExistsInDTO) {
                musician.removeMusicianGenre(musicianGenre);
            }
        }

        for (Instrument instrument : musicianDTO.getInstruments()) {
            boolean instrumentExistsInMusician = musician.getMusicianInstruments().stream()
                    .anyMatch(musicianInstrument -> musicianInstrument.getInstrument().equals(instrument));

            if (!instrumentExistsInMusician) {
                MusicianInstrument newMusicianInstrument = new MusicianInstrument(musician, instrument);
                musician.addMusicianInstrument(newMusicianInstrument);
            }
        }

        for (MusicianInstrument musicianInstrument : musician.getMusicianInstruments()) {
            boolean instrumentExistsInDTO = musicianDTO.getInstruments().stream()
                    .anyMatch(instrument -> musicianInstrument.getInstrument().equals(instrument));

            if (!instrumentExistsInDTO) {
                musician.removeMusicianInstrument(musicianInstrument);
            }
        }

        return mapper.toMusicianDTO(musicianRepository.save(musician));
    }

    public Page<MusicianDTO> getAll(Pageable pageable) {
        return musicianRepository.findAll(pageable).map(mapper::toMusicianDTO);
    }

    public MusicianDTO getById(int id) {
        var musician = musicianRepository.findMusicianById(id)
                .orElseThrow(() -> new MusicianException(MusicianError.MUSICIAN_NOT_FOUND));
        return mapper.toMusicianDTO(musician);
    }

    public Page<MusicianDTO> findMusiciansByFilters(
            List<String> cities,
            List<String> genres,
            List<String> instruments,
            Boolean isLookingForBand,
            Pageable pageable) {

        Specification<Musician> spec = Specification.where(MusicianSpecification.hasCities(cities))
                .and(MusicianSpecification.hasGenres(genres))
                .and(MusicianSpecification.hasInstruments(instruments))
                .and(MusicianSpecification.isLookingForBand(isLookingForBand));

        return musicianRepository.findAll(spec, pageable).map(mapper::toMusicianDTO);
    }

    @Transactional
    public void deleteMusician(int musicianId) {
        var musician = musicianRepository.findMusicianById(musicianId).orElseThrow(
                () -> new MusicianException(MusicianError.MUSICIAN_NOT_FOUND)
        );
        musicianRepository.deleteMusicianById(musicianId);
    }
}
