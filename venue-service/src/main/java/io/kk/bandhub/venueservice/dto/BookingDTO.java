package io.kk.bandhub.venueservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class BookingDTO {
    private Integer id;
    private Integer offerId;
    private Integer venueId;
    private String venueName;
    private Integer tenantId;
    private String tenantFirstName;
    private String tenantLastName;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate bookingDate;
    private LocalDate bookedDate;
    private BigDecimal pricePerHour;
}
