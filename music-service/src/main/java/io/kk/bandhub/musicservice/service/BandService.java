package io.kk.bandhub.musicservice.service;

import io.kk.bandhub.musicservice.dto.BandDTO;
import io.kk.bandhub.musicservice.dto.Mapper;
import io.kk.bandhub.musicservice.dto.MusicianDTO;
import io.kk.bandhub.musicservice.exception.BandNotFoundException;
import io.kk.bandhub.musicservice.exception.MusicianNotFoundException;
import io.kk.bandhub.musicservice.model.*;
import io.kk.bandhub.musicservice.repository.BandRepository;
import io.kk.bandhub.musicservice.repository.BandSpecification;
import io.kk.bandhub.musicservice.repository.MusicianRepository;
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
    private final Mapper mapper;

    public BandService(final BandRepository bandRepository, MusicianRepository musicianRepository, Mapper mapper) {
        this.bandRepository = bandRepository;
        this.musicianRepository = musicianRepository;
        this.mapper = mapper;
    }

    public BandDTO save(BandDTO bandDTO) {

        Musician founder = musicianRepository.findMusicianById(bandDTO.getFounderId())
                .orElseThrow(() ->new MusicianNotFoundException("Musician with given id was not found"));

        Band band = new Band();
        band.setName(bandDTO.getName());
        band.setBio(bandDTO.getBio());
        band.setCity(bandDTO.getCity());
        band.setIsLookingMembers(bandDTO.isLookingMembers());

        for(Genre genre : bandDTO.getGenres()) {
            BandGenre bandGenre = new BandGenre(band, genre);
            band.addBandGenre(bandGenre);
        }

        for(MusicianDTO musicianDTO : bandDTO.getMusicians()) {
            var musician = musicianRepository.findMusicianById(musicianDTO.getId()).orElseThrow(
                    () -> new MusicianNotFoundException("Musician with given id was not found")
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
                .orElseThrow(() -> new BandNotFoundException("Band with given id was not found"));
        return mapper.toBandDTO(band);
    }

    public Page<BandDTO> getAll(Pageable pageable) {
        return bandRepository.findAll(pageable).map(mapper::toBandDTO);
    }

    public Page<BandDTO> findBandByFilters(
            String name,
            List<String> cities,
            Integer founderId,
            List<Genre> genres,
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
                () ->new BandNotFoundException("Band with given id was not found"));

        band.setName(bandDTO.getName());
        band.setIsLookingMembers(bandDTO.isLookingMembers());
        band.setCity(bandDTO.getCity());
        band.setBio(bandDTO.getBio());

        for(Genre genre : bandDTO.getGenres()) {
            boolean genreExistsInBand = band.getBandGenres().stream()
                    .anyMatch(bandGenre -> bandGenre.getGenre().equals(genre));

            if (!genreExistsInBand) {
                BandGenre newBandGenre = new BandGenre(band, genre);
                band.addBandGenre(newBandGenre);
            }
        }

        for (BandGenre bandGenre : band.getBandGenres()) {
            boolean genreExistsInDTO = bandDTO.getGenres().stream()
                    .anyMatch(genre -> bandGenre.getGenre().equals(genre));

            if (!genreExistsInDTO) {
                band.removeBandGenre(bandGenre);
            }
        }

        for (MusicianDTO musicianDTO : bandDTO.getMusicians()) {
            boolean musicianExistsInBand = band.getBandMusicians().stream()
                    .anyMatch(bandMusician -> bandMusician.getMusician().getId().equals(musicianDTO.getId()));

            if (!musicianExistsInBand) {
                var musician = musicianRepository.findMusicianById(musicianDTO.getId()).orElseThrow(
                        () -> new MusicianNotFoundException("Musician with id " + musicianDTO.getId() + " not found")
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
                () -> new BandNotFoundException("Band with given id was not found"));
        bandRepository.deleteBandById(bandId);
    }

}
