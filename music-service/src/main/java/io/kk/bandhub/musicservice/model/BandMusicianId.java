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
public class BandMusicianId implements java.io.Serializable {
    private static final long serialVersionUID = 206066045302873398L;
    @NotNull
    @Column(name = "musician_id", nullable = false)
    private Integer musicianId;

    @NotNull
    @Column(name = "band_id", nullable = false)
    private Integer bandId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BandMusicianId entity = (BandMusicianId) o;
        return Objects.equals(this.bandId, entity.bandId) &&
                Objects.equals(this.musicianId, entity.musicianId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bandId, musicianId);
    }

}