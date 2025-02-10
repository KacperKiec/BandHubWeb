package io.kk.bandhub.musicservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "band_genre")
public class BandGenre {
    @EmbeddedId
    private BandGenreId id;

    @MapsId("bandId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "band_id", nullable = false)
    private Band band;

    @MapsId("genreId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    public BandGenre(Band band, Genre genre) {
        BandGenreId id = new BandGenreId();
        id.setGenreId(genre.getId());
        id.setBandId(band.getId());
        setId(id);
        this.band = band;
        this.genre = genre;
    }

    public BandGenre() {

    }
}