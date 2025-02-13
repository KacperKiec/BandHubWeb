package io.kk.bandhub.musicservice.repository.instrument;

import io.kk.bandhub.musicservice.model.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaInstrumentRepository extends JpaRepository<Instrument, Integer>, InstrumentRepository {

}
