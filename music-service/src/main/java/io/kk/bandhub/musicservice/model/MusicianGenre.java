package io.kk.bandhub.musicservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "musician_genre")
public class MusicianGenre {
    @EmbeddedId
    private MusicianGenreId id;

    @MapsId("musicianId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "musician_id", nullable = false)
    private Musician musician;

    @MapsId("genreId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    public MusicianGenre(Musician musician, Genre genre) {
        this.musician = musician;
        this.genre = genre;
        MusicianGenreId id = new MusicianGenreId();
        id.setMusicianId(musician.getId());
        id.setGenreId(genre.getId());
        setId(id);
    }

    public MusicianGenre() {

    }
}