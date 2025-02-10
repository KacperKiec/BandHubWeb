package io.kk.bandhub.musicservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "musician")
public class Musician {
    @Id
    @ColumnDefault("nextval('musician_id_seq'")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "is_looking_band", nullable = false)
    private Boolean isLookingBand;

    @Size(max = 255)
    @Column(name = "bio")
    private String bio;

    @OneToMany(mappedBy = "musician")
    private Set<MusicianGenre> musicianGenres;

    @OneToMany(mappedBy = "musician")
    private Set<MusicianInstrument> musicianInstruments;

    public void addMusicianGenre(MusicianGenre musicianGenre) {
        musicianGenres.add(musicianGenre);
    }

    public void removeMusicianGenre(MusicianGenre musicianGenre) {
        musicianGenres.remove(musicianGenre);
    }

    public void addMusicianInstrument(MusicianInstrument musicianInstrument) {
        musicianInstruments.add(musicianInstrument);
    }

    public void removeMusicianInstrument(MusicianInstrument musicianInstrument) {
        musicianInstruments.remove(musicianInstrument);
    }
}