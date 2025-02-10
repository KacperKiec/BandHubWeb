package io.kk.bandhub.musicservice.service;

import io.kk.bandhub.musicservice.dto.Mapper;
import io.kk.bandhub.musicservice.dto.MusicianDTO;
import io.kk.bandhub.musicservice.exception.MusicianNotFoundException;
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

    public MusicianDTO save(MusicianDTO musicianDTO) {
        Musician musician = new Musician();
        musician.setId(musicianDTO.getId());
        musician.setBio(musicianDTO.getBio());
        musician.setIsLookingBand(musicianDTO.isLookingBand());

        for(Genre genre : musicianDTO.getGenres()) {
            MusicianGenre musicianGenre = new MusicianGenre(musician, genre);
            musician.addMusicianGenre(musicianGenre);
        }

        for (Instrument instrument : musicianDTO.getInstruments()) {
            MusicianInstrument musicianInstrument = new MusicianInstrument(musician, instrument);
            musician.addMusicianInstrument(musicianInstrument);
        }

        var savedMusician = musicianRepository.save(musician);

        return mapper.toMusicianDTO(savedMusician);
    }

    @Transactional
    public MusicianDTO updateMusician(MusicianDTO musicianDTO) {
        var musician = musicianRepository.findMusicianById(musicianDTO.getId()).orElseThrow(
                () ->new MusicianNotFoundException("Musician with given id was not found"));

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
                .orElseThrow(() -> new MusicianNotFoundException("Musician with given id was not found"));
        return mapper.toMusicianDTO(musician);
    }

    public Page<MusicianDTO> findMusiciansByFilters(
            List<String> cities,
            List<Genre> genres,
            List<Instrument> instruments,
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
                () -> new MusicianNotFoundException("Musician with given id was not found")
        );
        musicianRepository.deleteMusicianById(musicianId);
    }
}
