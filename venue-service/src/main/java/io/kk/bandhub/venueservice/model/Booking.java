package io.kk.bandhub.venueservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('booking_id_seq'")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "offer_id", nullable = false)
    private Offer offer;

    @NotNull
    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

}