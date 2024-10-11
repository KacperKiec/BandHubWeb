package io.kk.bandhub.bandhubbackend.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@IdClass(MusicianBandId.class)
public class MusicianBand {

    @Id
    @ManyToOne
    @JoinColumn(name = "musician_id")
    private Musician musician;

    @Id
    @ManyToOne
    @JoinColumn(name = "band_id")
    private Band band;

    private LocalDate enterDate;

    public Musician getMusician() {
        return musician;
    }

    public void setMusician(Musician musician) {
        this.musician = musician;
    }

    public Band getBand() {
        return band;
    }

    public void setBand(Band band) {
        this.band = band;
    }

    public LocalDate getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(LocalDate enterDate) {
        this.enterDate = enterDate;
    }
}
