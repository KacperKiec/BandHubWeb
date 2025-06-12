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

    @NotNull
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Size(max = 150)
    @Column(name = "description", length = 150)
    private String description;

    @Size(max = 50)
    @NotNull
    @Column(name = "street", nullable = false, length = 50)
    private String street;

    @Size(max = 10)
    @NotNull
    @Column(name = "number", nullable = false, length = 10)
    private String number;

    @Size(max = 50)
    @NotNull
    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Size(max = 15)
    @NotNull
    @Column(name = "postal_code", nullable = false, length = 15)
    private String postalCode;

    @Size(max = 50)
    @NotNull
    @Column(name = "country", nullable = false, length = 50)
    private String country;
}