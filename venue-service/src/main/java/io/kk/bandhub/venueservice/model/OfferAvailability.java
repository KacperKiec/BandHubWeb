package io.kk.bandhub.venueservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.kk.bandhub.venueservice.utils.TimeRange;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "offer_availability")
public class OfferAvailability implements Comparable<OfferAvailability>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id", nullable = false)
    @JsonBackReference
    private Offer offer;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    public OfferAvailability() {

    }

    public OfferAvailability(Offer offer) {
        this.offer = offer;
        this.startTime = offer.getStartTime();
        this.endTime = offer.getEndTime();
    }

    public OfferAvailability(Offer offer, LocalTime startTime, LocalTime endTime) {
        this.offer = offer;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public int compareTo(OfferAvailability o) {
        return this.startTime.compareTo(o.startTime);
    }

    public boolean overlaps(TimeRange other) {
        return !this.endTime.isBefore(other.getStartTime()) && !this.startTime.isAfter(other.getEndTime());
    }

    public boolean match(TimeRange other) {
        return !this.startTime.isAfter(other.getStartTime()) && !this.endTime.isBefore(other.getStartTime());
    }
}

