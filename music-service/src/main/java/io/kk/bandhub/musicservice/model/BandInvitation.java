package io.kk.bandhub.musicservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "band_invitations")
@NoArgsConstructor
public class BandInvitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('band_invitations_id_seq'")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_id", nullable = false)
    private Band band;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_musician_id", nullable = false)
    private Musician musician;

    @NotNull
    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    public BandInvitation(Band band, Musician musician, RequestStatus status, LocalDate createdAt) {
        this.band = band;
        this.musician = musician;
        this.status = status;
        this.createdAt = createdAt;
    }
}