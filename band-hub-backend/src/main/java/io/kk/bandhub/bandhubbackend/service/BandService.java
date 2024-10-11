package io.kk.bandhub.bandhubbackend.service;

import io.kk.bandhub.bandhubbackend.model.Band;
import io.kk.bandhub.bandhubbackend.model.Instrument;
import io.kk.bandhub.bandhubbackend.model.MusicalGenre;
import io.kk.bandhub.bandhubbackend.model.Musician;
import io.kk.bandhub.bandhubbackend.repository.BandRepository;
import io.kk.bandhub.bandhubbackend.repository.BandSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for provide data and method for BandController
 * */
@Service
public class BandService {
    private final BandRepository bandRepository;

    public BandService(final BandRepository bandRepository) {
        this.bandRepository = bandRepository;
    }

    public Band save(Band band) {
        return bandRepository.save(band);
    }

    public Band getById(int id) {
        return bandRepository.findBandById(id);
    }

    public List<Band> getAll() {
        return bandRepository.findAll();
    }

    public List<Band> findBandByFilters(String name, List<String> cities, Musician founder, List<MusicalGenre> genres, List<Instrument> requestedInstruments, List<Instrument> currentInstruments) {
        Specification<Band> spec = Specification.where(BandSpecification.hasName(name)
                .and(BandSpecification.hasCity(cities))
                .and(BandSpecification.hasFounderId(founder.getId()))
                .and(BandSpecification.hasGenres(genres))
                .and(BandSpecification.hasRequestedInstruments(requestedInstruments))
                .and(BandSpecification.hasInstruments(currentInstruments)));
        return bandRepository.findAll(spec);
    }

}
