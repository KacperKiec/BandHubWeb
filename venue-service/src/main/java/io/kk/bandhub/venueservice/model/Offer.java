package io.kk.bandhub.venueservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "offer")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('offer_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @NotNull
    @Column(name = "price_per_hour", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerHour;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @NotNull
    @Column(name = "status", columnDefinition = "offer_status not null")
    @Enumerated(EnumType.STRING)
    private OfferStatus status;

}