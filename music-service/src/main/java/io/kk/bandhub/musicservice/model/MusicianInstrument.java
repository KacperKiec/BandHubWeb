package io.kk.bandhub.musicservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "musician_instrument")
public class MusicianInstrument {
    @EmbeddedId
    private MusicianInstrumentId id;

    @MapsId("musicianId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "musician_id", nullable = false)
    private Musician musician;

    @MapsId("instrumentId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "instrument_id", nullable = false)
    private Instrument instrument;

    public MusicianInstrument(Musician musician, Instrument instrument) {
        this.musician = musician;
        this.instrument = instrument;
        MusicianInstrumentId id = new MusicianInstrumentId();
        id.setMusicianId(musician.getId());
        id.setInstrumentId(instrument.getId());
        setId(id);
    }

    public MusicianInstrument() {

    }
}