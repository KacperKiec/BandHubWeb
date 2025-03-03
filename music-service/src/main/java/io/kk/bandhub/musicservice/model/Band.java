package io.kk.bandhub.musicservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "band")
public class Band {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('band_id_seq'")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 30)
    @NotNull
    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @NotNull
    @Column(name = "is_looking_members", nullable = false)
    private Boolean isLookingMembers;

    @Size(max = 20)
    @Column(name = "city", length = 20)
    private String city;

    @Size(max = 400)
    @Column(name = "bio", length = 400)
    private String bio;

    @OneToMany(mappedBy = "band", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BandMusician> bandMusicians = new HashSet<>();

    @OneToMany(mappedBy = "band", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BandGenre> bandGenres = new HashSet<>();

    public void addBandMusician(BandMusician bandMusician) {
        bandMusicians.add(bandMusician);
    }

    public void removeBandMusician(BandMusician bandMusician) {
        bandMusicians.remove(bandMusician);
    }

    public void addBandGenre(BandGenre bandGenre) {
        bandGenres.add(bandGenre);
    }

    public void removeBandGenre(BandGenre bandGenre) {
        bandGenres.remove(bandGenre);
    }

}