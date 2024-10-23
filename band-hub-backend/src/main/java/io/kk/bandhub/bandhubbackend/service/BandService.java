package io.kk.bandhub.bandhubbackend.service;

import io.kk.bandhub.bandhubbackend.exception.BandNotFoundException;
import io.kk.bandhub.bandhubbackend.exception.MusicianNotFoundException;
import io.kk.bandhub.bandhubbackend.model.*;
import io.kk.bandhub.bandhubbackend.repository.BandRepository;
import io.kk.bandhub.bandhubbackend.repository.BandSpecification;
import io.kk.bandhub.bandhubbackend.repository.MusicianRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for provide data and method for BandController
 * */
@Service
public class BandService {
    private final BandRepository bandRepository;
    private final MusicianRepository musicianRepository;

    public BandService(final BandRepository bandRepository, MusicianRepository musicianRepository) {
        this.bandRepository = bandRepository;
        this.musicianRepository = musicianRepository;
    }

    public BandDTO save(Band band) {

        Musician founder = musicianRepository.findMusicianById(band.getFounderId())
                .orElseThrow(() ->new MusicianNotFoundException("Musician with given id was not found"));

        band.setFounder(founder);
        band.setFounderId(founder.getId());
        founder.addBand(band);

        var savedBand = bandRepository.save(band);

        return toDTO(savedBand);
    }

    public BandDTO getById(int id) {
        var band = bandRepository.findBandById(id)
                .orElseThrow(() -> new BandNotFoundException("Band with given id was not found"));
        return toDTO(band);
    }

    public Page<BandDTO> getAll(Pageable pageable) {
        return bandRepository.findAll(pageable).map(this::toDTO);
    }

    public Page<BandDTO> findBandByFilters(
            String name,
            List<String> cities,
            Integer founderId,
            List<MusicalGenre> genres,
            List<Instrument> requestedInstruments,
            List<Instrument> currentInstruments,
            Boolean isLookingMembers,
            Pageable pageable) {

        Specification<Band> spec = Specification.where(BandSpecification.hasName(name)
                .and(BandSpecification.hasCity(cities))
                .and(BandSpecification.hasFounderId(founderId))
                .and(BandSpecification.hasGenres(genres))
                .and(BandSpecification.hasRequestedInstruments(requestedInstruments))
                .and(BandSpecification.hasInstruments(currentInstruments))
                .and(BandSpecification.isLookingMembers(isLookingMembers)));
        return bandRepository.findAll(spec, pageable).map(this::toDTO);
    }

    private BandDTO toDTO(Band band) {
        BandDTO dto = new BandDTO();
        dto.setId(band.getId());
        dto.setBandName(band.getName());
        dto.setMaxMembers(band.getMaxMembers());
        dto.setCity(band.getCity());
        dto.setBio(band.getBio());
        dto.setLookingMembers(band.isLookingMembers());

        var musician = musicianRepository.findMusicianById(band.getFounderId())
                .orElseThrow(() -> new MusicianNotFoundException("Musician with given id was not found"));
        dto.setFounderName(musician.getName());

        dto.setRequestedInstruments(band.getRequestedInstruments());
        dto.setCurrentInstruments(band.getCurrentInstruments());
        dto.setGenres(band.getGenres());

        List<Musician> members = band.getMembers() != null ? band.getMembers() : Collections.emptyList();

        List<String> memberNames = members.stream()
                .map(Musician::getName)
                .collect(Collectors.toList());

        dto.setMembers(memberNames);
        return dto;
    }

}
