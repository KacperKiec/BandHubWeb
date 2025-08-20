package io.kk.bandhub.eventservice.integration;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.kk.bandhub.eventservice.dto.BookingEventDataDTO;
import io.kk.bandhub.eventservice.dto.OfferDTO;
import io.kk.bandhub.eventservice.dto.VenueDTO;
import io.kk.bandhub.eventservice.exception.RegistrationException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "VENUE-SERVICE")
public interface VenueServiceClient {

    @GetMapping("/event-data/internal/{id}")
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallbackGetData")
    BookingEventDataDTO getVenueEventData(@PathVariable Integer id);

    @GetMapping("/event-data/internal/{id}")
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallbackGetDataReg")
    BookingEventDataDTO getVenueEventDataReg(@PathVariable Integer id);

    @GetMapping(value = "/venue/internal/ids")
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallbackGetVenues")
    List<VenueDTO> getVenuesByIds(@RequestParam List<Integer> ids);

    @GetMapping(value = "/venue/internal/{id}")
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallbackGetVenue")
    VenueDTO getVenueById(@PathVariable Integer id);

    @GetMapping(value = "/offer/internal/venue/{id}")
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallbackGetOffers")
    List<OfferDTO> getOffersByVenueAndDate(@PathVariable Integer id, @RequestParam LocalDate date);

    default BookingEventDataDTO fallbackGetData(Integer id, Throwable throwable) {
        BookingEventDataDTO b = new BookingEventDataDTO();
        b.setVenueId(null);
        b.setVenueName("unknown");
        b.setTenantId(null);
        b.setStartTime(null);
        b.setEndTime(null);

        b.setStreet("unknown");
        b.setCity("unknown");
        b.setCountry("unknown");
        b.setPostalCode("unknown");
        b.setNumber("unknown");
        return b;
    }

    default BookingEventDataDTO fallbackGetDataReg(Throwable throwable) {
        if (throwable instanceof feign.FeignException.ServiceUnavailable) {
            throw new RegistrationException("Registration failed because of service unavailability");
        } else if (throwable instanceof feign.FeignException.NotFound) {
            throw new RegistrationException("Registration failed because of bad data (user does not exist)");
        } else {
            throw new RegistrationException("Registration failed, something went wrong");
        }
    }

    default List<VenueDTO> fallbackGetVenues(List<Integer> ids, Throwable throwable) {
        return List.of();
    }

    default VenueDTO fallbackGetVenue(Integer id, Throwable throwable) {
        return null;
    }

    default List<OfferDTO> fallbackGetOffers(Integer id, LocalDate date, Throwable throwable) {
        throw new RuntimeException("Something went wrong");
    }
}
