package io.kk.bandhub.eventservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('event_id_seq'")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "band_id", nullable = false)
    private Integer bandId;

    @NotNull
    @Column(name = "venue_id", nullable = false)
    private Integer venueId;

    @NotNull
    @Column(name = "booking_id", nullable = false)
    private Integer bookingId;

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
    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "ticket_price", precision = 10, scale = 2)
    private BigDecimal ticketPrice;
}