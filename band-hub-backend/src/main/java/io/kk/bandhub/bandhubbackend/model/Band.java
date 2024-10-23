package io.kk.bandhub.bandhubbackend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


/**
 * Class represents entity in database
 * */
@Data
@Entity
@Table(name = "band")
public class Band {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int maxMembers;
    private boolean isLookingMembers;
    private String city;
    private String bio;

    @ElementCollection(targetClass = MusicalGenre.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "band_genre", joinColumns = @JoinColumn(name = "band_id"))
    @Column(name = "genre")
    private List<MusicalGenre> genres;

    @ElementCollection(targetClass = Instrument.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "band_requested_instrument", joinColumns = @JoinColumn(name = "band_id"))
    @Column(name = "instrument")
    private List<Instrument> requestedInstruments;

    @ElementCollection(targetClass = Instrument.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "band_current_instrument", joinColumns = @JoinColumn(name = "band_id"))
    @Column(name = "instrument")
    private List<Instrument> currentInstruments;

    @ManyToMany
    @JoinTable(
            name = "musician_band",
            joinColumns = @JoinColumn(name = "band_id"),
            inverseJoinColumns = @JoinColumn(name = "musician_id")
    )
    private List<Musician> members;

    @ManyToOne
    @Transient
    private Musician founder;

    @JoinColumn(name = "founder_id")
    private Integer founderId;

    public Band() {

    }

    public void addMember(Musician musician) {
        this.members.add(musician);
    }

}
