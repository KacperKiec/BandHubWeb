package io.kk.bandhub.venueservice.controller;

import io.kk.bandhub.venueservice.dto.OfferDTO;
import io.kk.bandhub.venueservice.service.OfferService;
import io.kk.bandhub.venueservice.utils.OfferStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/offer/public")
@RestController
@RequiredArgsConstructor
public class PublicOfferController {

    private final OfferService offerService;

    @GetMapping(value = "/search")
    ResponseEntity<List<OfferDTO>> searchOffers(
            @RequestParam(required = false) List<String> cities,
            @RequestParam(required = false) OfferStatus status,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) Double minLatitude,
            @RequestParam(required = false) Double maxLatitude,
            @RequestParam(required = false) Double minLongitude,
            @RequestParam(required = false) Double maxLongitude) {

        var result = offerService.searchOffers(cities, status, date, minLatitude, maxLatitude, minLongitude, maxLongitude);
        return ResponseEntity.ok(result);
    }
}
