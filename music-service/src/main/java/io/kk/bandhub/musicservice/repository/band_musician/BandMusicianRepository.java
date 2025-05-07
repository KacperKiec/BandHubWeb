package io.kk.bandhub.musicservice.repository.band_musician;

import io.kk.bandhub.musicservice.model.BandMusician;

import java.util.List;

public interface BandMusicianRepository {
    List<BandMusician> getBandMusiciansByMusicianId(Integer musicianId);
}
