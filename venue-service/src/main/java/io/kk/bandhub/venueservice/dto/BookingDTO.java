package io.kk.bandhub.venueservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingDTO {

    private Integer id;

    @NotNull
    private Integer offerId;
    private String venueName;

    private EventDTO event;

    @NotNull
    private Integer tenantId;

    private String tenantFirstName;
    private String tenantLastName;
}
