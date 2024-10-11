package io.kk.bandhub.bandhubbackend.service;

import io.kk.bandhub.bandhubbackend.model.Instrument;
import io.kk.bandhub.bandhubbackend.model.MusicalGenre;
import io.kk.bandhub.bandhubbackend.model.Musician;
import io.kk.bandhub.bandhubbackend.model.Sex;
import io.kk.bandhub.bandhubbackend.repository.MusicianRepository;
import io.kk.bandhub.bandhubbackend.repository.MusicianSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Musician save(Musician musician) {
        return musicianRepository.save(musician);
    }

    public List<Musician> getAll() {
        return musicianRepository.findAll();
    }

    public Musician getById(int id) {
        return musicianRepository.findMusicianById(id);
    }

    public List<Musician> findMusiciansByFilters(String name, List<String> cities, List<MusicalGenre> genres, List<Instrument> instruments, boolean isLookingForBand, String sex) {
        Specification<Musician> spec = Specification.where(MusicianSpecification.hasCities(cities))
                .and(MusicianSpecification.hasGenres(genres))
                .and(MusicianSpecification.hasInstruments(instruments))
                .and(MusicianSpecification.isLookingForBand(isLookingForBand))
                .and(MusicianSpecification.hasSex(sex))
                .and(MusicianSpecification.hasName(name));

        return musicianRepository.findAll(spec);
    }

}
