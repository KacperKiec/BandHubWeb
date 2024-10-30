package io.kk.bandhub.bandhubbackend.service;

import io.kk.bandhub.bandhubbackend.exception.BandNotFoundException;
import io.kk.bandhub.bandhubbackend.exception.MusicianNotFoundException;
import io.kk.bandhub.bandhubbackend.model.*;
import io.kk.bandhub.bandhubbackend.repository.BandRepository;
import io.kk.bandhub.bandhubbackend.repository.MusicianRepository;
import io.kk.bandhub.bandhubbackend.repository.MusicianSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for provide data and methods for MusicianController
 * */
@Service
public class MusicianService {
    private final MusicianRepository musicianRepository;
    private final BandRepository bandRepository;

    public MusicianService(MusicianRepository musicianRepository, BandRepository bandRepository) {
        this.musicianRepository = musicianRepository;
        this.bandRepository = bandRepository;
    }

    public MusicianDTO save(Musician musician) {
        var musicianSaved = musicianRepository.save(musician);
        return toDTO(musicianSaved);
    }

    public MusicianDTO updateMusician(MusicianDTO musicianDTO) {
        var musician = musicianRepository.findMusicianById(musicianDTO.getId()).orElseThrow(
                () ->new MusicianNotFoundException("Musician with given id was not found"));

        musician.setName(musicianDTO.getName());
        musician.setEmail(musicianDTO.getEmail());
        musician.setPassword(musicianDTO.getPassword());
        musician.setAge(musicianDTO.getAge());
        musician.setCity(musicianDTO.getCity());
        musician.setInstruments(musicianDTO.getInstruments());
        musician.setGenres(musicianDTO.getGenres());
        musician.setSex(musicianDTO.getSex());
        musician.setBio(musicianDTO.getBio());
        musician.setLookingBand(musicianDTO.isLookingBand());

        return toDTO(musicianRepository.save(musician));
    }

    public MusicianDTO updateMusicianBand(MusicianDTO musicianDTO, int bandId) {
        var musician = musicianRepository.findMusicianById(musicianDTO.getId()).orElseThrow(
                () ->new MusicianNotFoundException("Musician with given id was not found")
        );

        var band = bandRepository.findBandById(bandId).orElseThrow(
                () ->new BandNotFoundException("Band with given id was not found")
        );

        musician.addBand(band);
        band.addMember(musician);

        bandRepository.save(band);
        return toDTO(musicianRepository.save(musician));
    }

    public Page<MusicianDTO> getAll(Pageable pageable) {
        return musicianRepository.findAll(pageable).map(this::toDTO);
    }

    public MusicianDTO getById(int id) {
        var musician = musicianRepository.findMusicianById(id)
                .orElseThrow(() -> new MusicianNotFoundException("Musician with given id was not found"));
        return toDTO(musician);
    }

    public Page<MusicianDTO> findMusiciansByFilters(
            String name,
            List<String> cities,
            List<MusicalGenre> genres,
            List<Instrument> instruments,
            Boolean isLookingForBand, String sex,
            Pageable pageable) {

        Specification<Musician> spec = Specification.where(MusicianSpecification.hasCities(cities))
                .and(MusicianSpecification.hasGenres(genres))
                .and(MusicianSpecification.hasInstruments(instruments))
                .and(MusicianSpecification.isLookingForBand(isLookingForBand))
                .and(MusicianSpecification.hasSex(sex))
                .and(MusicianSpecification.hasName(name));

        return musicianRepository.findAll(spec, pageable).map(this::toDTO);
    }

    private MusicianDTO toDTO(Musician musician) {
        MusicianDTO dto = new MusicianDTO();
        dto.setId(musician.getId());
        dto.setName(musician.getName());
        dto.setEmail(musician.getEmail());
        dto.setPassword(musician.getPassword());
        dto.setCity(musician.getCity());
        dto.setInstruments(musician.getInstruments());
        dto.setGenres(musician.getGenres());
        dto.setSex(musician.getSex());
        dto.setLookingBand(musician.isLookingBand());
        dto.setAge(musician.getAge());

        List<Band> bands = musician.getBands() != null ? musician.getBands() : Collections.emptyList();

        List<String> bandNames = bands.stream()
                .map(Band::getName)
                .collect(Collectors.toList());
        dto.setBands(bandNames);

        return dto;
    }
}
