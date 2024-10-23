package io.kk.bandhub.bandhubbackend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

/**
 * Class represents entity in database
 * */
@Data
@Entity
@Table(name = "musician")
public class Musician {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String password;
    private int age;

    @Enumerated(EnumType.STRING)
    private Sex sex;
    private String city;
    private boolean isLookingBand;
    private String bio;

    @ElementCollection(targetClass = Instrument.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "musician_instrument", joinColumns = @JoinColumn(name = "musician_id"))
    @Column(name = "instrument")
    private List<Instrument> instruments;

    @ElementCollection(targetClass = MusicalGenre.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "musician_genre", joinColumns = @JoinColumn(name = "musician_id"))
    @Column(name = "genre")
    private List<MusicalGenre> genres;

    @ManyToMany
    @JoinTable(
            name = "musician_band",
            joinColumns = @JoinColumn(name = "musician_id"),
            inverseJoinColumns = @JoinColumn(name = "band_id")
    )
    private List<Band> bands;

    public Musician() {

    }

    public void addBand(Band band) {
        this.bands.add(band);
    }
}
