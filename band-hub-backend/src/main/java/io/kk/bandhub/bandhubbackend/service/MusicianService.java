package io.kk.bandhub.bandhubbackend.service;

import io.kk.bandhub.bandhubbackend.exception.MusicianNotFoundException;
import io.kk.bandhub.bandhubbackend.model.*;
import io.kk.bandhub.bandhubbackend.repository.MusicianRepository;
import io.kk.bandhub.bandhubbackend.repository.MusicianSpecification;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public MusicianService(MusicianRepository musicianRepository) {
        this.musicianRepository = musicianRepository;
    }

    public MusicianDTO save(Musician musician) {
        var musicianSaved = musicianRepository.save(musician);
        return toDTO(musicianSaved);
    }

    public List<MusicianDTO> getAll() {
        return musicianRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public MusicianDTO getById(int id) {
        var musician = musicianRepository.findMusicianById(id)
                .orElseThrow(() -> new MusicianNotFoundException("Musician with given id was not found"));
        return toDTO(musician);
    }

    public List<MusicianDTO> findMusiciansByFilters(
            String name,
            List<String> cities,
            List<MusicalGenre> genres,
            List<Instrument> instruments,
            Boolean isLookingForBand, String sex) {

        Specification<Musician> spec = Specification.where(MusicianSpecification.hasCities(cities))
                .and(MusicianSpecification.hasGenres(genres))
                .and(MusicianSpecification.hasInstruments(instruments))
                .and(MusicianSpecification.isLookingForBand(isLookingForBand))
                .and(MusicianSpecification.hasSex(sex))
                .and(MusicianSpecification.hasName(name));

        return musicianRepository.findAll(spec).stream().map(this::toDTO).collect(Collectors.toList());
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

        List<Band> bands = musician.getBands() != null ? musician.getBands() : Collections.emptyList();

        List<String> bandNames = bands.stream()
                .map(Band::getName)
                .collect(Collectors.toList());
        dto.setBands(bandNames);

        return dto;
    }
}
