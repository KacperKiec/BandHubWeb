package io.kk.bandhub.eventservice.integration;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.kk.bandhub.eventservice.dto.BandEventDataDTO;
import io.kk.bandhub.eventservice.exception.RegistrationException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "MUSIC-SERVICE")
public interface MusicServiceClient {

    @GetMapping("/band/internal/evt/{id}")
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallbackGetBand")
    BandEventDataDTO getBandById(@PathVariable Integer id);

    @GetMapping("/band/internal/evt/name")
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallbackGetBandByName")
    BandEventDataDTO getBandByName(@RequestParam String name);

    @GetMapping("/band/internal/evt/{id}")
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallbackGetBandReg")
    BandEventDataDTO getBandReg(@PathVariable Integer id);

    @GetMapping("/band/internal/{id}/members")
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallbackGetBandMembersIds")
    List<Integer> getBandMembersIds(@PathVariable Integer id);

    default BandEventDataDTO fallbackGetBand(Integer id, Throwable throwable) {
        BandEventDataDTO bandEventDataDTO = new BandEventDataDTO();
        bandEventDataDTO.setId(id);
        bandEventDataDTO.setFounderId(null);
        bandEventDataDTO.setName("unknown");
        return bandEventDataDTO;
    }

    default BandEventDataDTO fallbackGetBandByName(String name, Throwable throwable) {
        BandEventDataDTO bandEventDataDTO = new BandEventDataDTO();
        bandEventDataDTO.setId(null);
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

    default List<Integer> fallbackGetBandMembersIds(Integer id, Throwable throwable) {
        throw new RuntimeException("Something went wrong");
    }
}
