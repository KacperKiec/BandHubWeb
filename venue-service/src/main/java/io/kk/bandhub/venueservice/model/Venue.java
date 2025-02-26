package io.kk.bandhub.venueservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "venue")
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('venue_id_seq'")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "owner_id", nullable = false)
    private Integer ownerId;

    @Size(max = 40)
    @NotNull
    @Column(name = "name", nullable = false, length = 40)
    private String name;

    @Size(max = 30)
    @NotNull
    @Column(name = "city", nullable = false, length = 30)
    private String city;

    @Size(max = 50)
    @NotNull
    @Column(name = "address", nullable = false, length = 50)
    private String address;

    @NotNull
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Size(max = 150)
    @Column(name = "description", length = 150)
    private String description;

}