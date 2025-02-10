package io.kk.bandhub.musicservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "genre")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('genre_id_seq'")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 40)
    @NotNull
    @Column(name = "name", nullable = false, length = 40)
    private String name;

}