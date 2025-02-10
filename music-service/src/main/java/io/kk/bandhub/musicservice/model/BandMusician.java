package io.kk.bandhub.musicservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "band_musician")
public class BandMusician {
    @EmbeddedId
    private BandMusicianId id;

    @MapsId("musicianId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "musician_id", nullable = false)
    private Musician musician;

    @MapsId("bandId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "band_id", nullable = false)
    private Band band;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @Column(name = "is_founder")
    private boolean isFounder;

    public BandMusician(Band band, Musician musician, LocalDate joinDate, boolean isFounder) {
        this.band = band;
        this.musician = musician;
        this.joinDate = joinDate;
        this.isFounder = isFounder;
        BandMusicianId id = new BandMusicianId();
        id.setBandId(band.getId());
        id.setMusicianId(musician.getId());
        setId(id);
    }

    public BandMusician() {

    }
}