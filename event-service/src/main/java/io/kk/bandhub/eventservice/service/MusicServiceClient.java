package io.kk.bandhub.eventservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.kk.bandhub.eventservice.dto.BandDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "MUSIC-SERVICE")
public interface MusicServiceClient {

    @GetMapping("/band/evt/{id}")
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallbackGetAccount")
    BandDTO getBand(@PathVariable Integer id);

    default BandDTO fallbackGetAccount(Integer id, Throwable throwable) {
        BandDTO bandDTO = new BandDTO();
        bandDTO.setId(id);
        bandDTO.setName("unknown");
        bandDTO.setBio("unknown");
        bandDTO.setGenres(null);
        return bandDTO;
    }
}
