package io.kk.bandhub.bandhubbackend.model;

import java.io.Serializable;
import java.util.Objects;

public class MusicianBandId implements Serializable {

    private int musician;
    private int band;

    public MusicianBandId() {}

    public MusicianBandId(int musician, int band) {
        this.musician = musician;
        this.band = band;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicianBandId that = (MusicianBandId) o;
        return Objects.equals(musician, that.musician) && Objects.equals(band, that.band);
    }

    @Override
    public int hashCode() {
        return Objects.hash(musician, band);
    }
}

