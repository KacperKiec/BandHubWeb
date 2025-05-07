package io.kk.bandhub.musicservice.repository.band_musician;

import io.kk.bandhub.musicservice.model.BandMusician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaBandMusicianRepository extends JpaRepository<BandMusician, Integer>, BandMusicianRepository {
}
