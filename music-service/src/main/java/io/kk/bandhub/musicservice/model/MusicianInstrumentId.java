package io.kk.bandhub.musicservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class MusicianInstrumentId implements java.io.Serializable {
    private static final long serialVersionUID = 7373638474636970889L;
    @NotNull
    @Column(name = "musician_id", nullable = false)
    private Integer musicianId;

    @NotNull
    @Column(name = "instrument_id", nullable = false)
    private Integer instrumentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MusicianInstrumentId entity = (MusicianInstrumentId) o;
        return Objects.equals(this.instrumentId, entity.instrumentId) &&
                Objects.equals(this.musicianId, entity.musicianId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instrumentId, musicianId);
    }

}