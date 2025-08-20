package io.kk.bandhub.venueservice.controller;

import io.kk.bandhub.venueservice.dto.OfferDTO;
import io.kk.bandhub.venueservice.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/offer/internal")
@RestController
@RequiredArgsConstructor
public class InternalOfferController {

    private final OfferService offerService;

    @GetMapping(value = "/venue/{id}")
    ResponseEntity<List<OfferDTO>> getOffersByVenueAndDate(@PathVariable Integer id, @RequestParam LocalDate date) {
        var result = offerService.getNotCanceledOffersByVenueAndDate(id, date);
        return ResponseEntity.ok(result);
    }
}
