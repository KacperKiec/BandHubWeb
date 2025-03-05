package io.kk.bandhub.eventservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.kk.bandhub.eventservice.dto.VenueDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "VENUE-SERVICE")
public interface VenueServiceClient {

    @GetMapping("/venue/{id}")
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallbackGetVenue")
    VenueDTO getVenue(@PathVariable Integer id);

    default VenueDTO fallbackGetVenue(Integer id, Throwable throwable) {
        VenueDTO v = new VenueDTO();
        v.setId(id);
        v.setName("unknown");
        v.setDescription("unknown");
        v.setAddress("unknown");
        v.setCity("unknown");
        v.setCapacity(0);
        v.setOwnerId(null);
        return v;
    }
}
