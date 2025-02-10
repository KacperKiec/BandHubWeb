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
public class MusicianGenreId implements java.io.Serializable {
    private static final long serialVersionUID = -7949584884775125994L;
    @NotNull
    @Column(name = "musician_id", nullable = false)
    private Integer musicianId;

    @NotNull
    @Column(name = "genre_id", nullable = false)
    private Integer genreId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MusicianGenreId entity = (MusicianGenreId) o;
        return Objects.equals(this.genreId, entity.genreId) &&
                Objects.equals(this.musicianId, entity.musicianId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genreId, musicianId);
    }

}