package io.kk.bandhub.eventservice.integration;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.kk.bandhub.eventservice.dto.BookingEventDataDTO;
import io.kk.bandhub.eventservice.exception.RegistrationException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "VENUE-SERVICE")
public interface VenueServiceClient {

    @GetMapping("/event-data/{id}")
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallbackGetBooking")
    BookingEventDataDTO getBookingEventData(@PathVariable Integer id);

    @GetMapping("/event-data/{id}")
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallbackGetBookingReg")
    BookingEventDataDTO getBookingEventDataReg(@PathVariable Integer id);

    default BookingEventDataDTO fallbackGetBooking(Integer id, Throwable throwable) {
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

    default BookingEventDataDTO fallbackGetBookingReg(Throwable throwable) {
        if (throwable instanceof feign.FeignException.ServiceUnavailable) {
            throw new RegistrationException("Registration failed because of service unavailability");
        } else if (throwable instanceof feign.FeignException.NotFound) {
            throw new RegistrationException("Registration failed because of bad data (user does not exist)");
        } else {
            throw new RegistrationException("Registration failed, something went wrong");
        }
    }
}
