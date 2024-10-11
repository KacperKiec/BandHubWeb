package io.kk.bandhub.bandhubbackend.model;

import jakarta.persistence.*;

import java.util.List;

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
    private Sex sex;
    private String city;

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

    private boolean isLookingBand;
    private String bio;

    public Musician() {

    }

    public Musician(String name, String email, String password, int age, Sex sex, String city) {
        setName(name);
        setEmail(email);
        setPassword(password);
        setAge(age);
        setSex(sex);
        setCity(city);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<MusicalGenre> getGenres() {
        return genres;
    }

    public void setGenres(List<MusicalGenre> genres) {
        this.genres = genres;
    }

    public boolean isLookingBand() {
        return isLookingBand;
    }

    public void setLookingBand(boolean lookingBand) {
        isLookingBand = lookingBand;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<Instrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<Instrument> instruments) {
        this.instruments = instruments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
