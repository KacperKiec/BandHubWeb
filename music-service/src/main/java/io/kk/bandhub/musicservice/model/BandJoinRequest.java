package io.kk.bandhub.musicservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "band_join_requests")
@NoArgsConstructor
public class BandJoinRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('band_join_requests_id_seq'")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_id", nullable = false)
    private Band band;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    private Musician musician;

    @NotNull
    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    public BandJoinRequest(Band band, Musician musician, RequestStatus status, LocalDate createdAt) {
        this.band = band;
        this.musician = musician;
        this.status = status;
        this.createdAt = createdAt;
    }
}