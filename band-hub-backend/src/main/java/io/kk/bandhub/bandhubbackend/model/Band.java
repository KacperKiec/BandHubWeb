package io.kk.bandhub.bandhubbackend.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "band")
public class Band {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

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

    @ManyToOne
    @JoinColumn(name = "founder_id")
    private Musician founder;


    private int maxMembers;
    private boolean isLookingMembers;
    private String city;
    private String bio;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MusicalGenre> getGenres() {
        return genres;
    }

    public void setGenres(List<MusicalGenre> genres) {
        this.genres = genres;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }

    public Musician getFounder() {
        return founder;
    }

    public void setFounder(Musician founder) {
        this.founder = founder;
    }

    public List<Instrument> getRequestedInstruments() {
        return requestedInstruments;
    }

    public void setRequestedInstruments(List<Instrument> requestedInstruments) {
        this.requestedInstruments = requestedInstruments;
    }

    public List<Instrument> getCurrentInstruments() {
        return currentInstruments;
    }

    public void setCurrentInstruments(List<Instrument> currentInstruments) {
        this.currentInstruments = currentInstruments;
    }

    public boolean isLookingMembers() {
        return isLookingMembers;
    }

    public void setLookingMembers(boolean lookingMembers) {
        isLookingMembers = lookingMembers;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
