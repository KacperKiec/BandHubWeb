package io.kk.bandhub.eventservice.integration;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.kk.bandhub.eventservice.dto.BandEventDataDTO;
import io.kk.bandhub.eventservice.exception.RegistrationException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "MUSIC-SERVICE")
public interface MusicServiceClient {

    @GetMapping("/band/evt/{id}")
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallbackGetBand")
    BandEventDataDTO getBand(@PathVariable Integer id);

    @GetMapping("/band/evt/{id}")
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallbackGetBandReg")
    BandEventDataDTO getBandReg(@PathVariable Integer id);

    default BandEventDataDTO fallbackGetBand(Integer id, Throwable throwable) {
        BandEventDataDTO bandEventDataDTO = new BandEventDataDTO();
        bandEventDataDTO.setId(id);
        bandEventDataDTO.setFounderId(null);
        bandEventDataDTO.setName("unknown");
        return bandEventDataDTO;
    }

    default BandEventDataDTO fallbackGetBandReg(Throwable throwable) {
        if (throwable instanceof feign.FeignException.ServiceUnavailable) {
            throw new RegistrationException("Registration failed because of service unavailability");
        } else if (throwable instanceof feign.FeignException.NotFound) {
            throw new RegistrationException("Registration failed because of bad data (user does not exist)");
        } else {
            throw new RegistrationException("Registration failed, something went wrong");
        }
    }
}
