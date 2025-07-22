package io.kk.bandhub.musicservice.repository.instrument;

import io.kk.bandhub.musicservice.model.Instrument;

import java.util.List;
import java.util.Optional;

public interface InstrumentRepository {
    Instrument save(Instrument instrument);
    void deleteById(Integer id);
    Optional<Instrument> findById(Integer id);
    Optional<Instrument> findByName(String name);
    List<Instrument> findAll();
}
