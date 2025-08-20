package io.kk.bandhub.venueservice.controller;

import io.kk.bandhub.venueservice.dto.OfferDTO;
import io.kk.bandhub.venueservice.dto.OfferRequestDTO;
import io.kk.bandhub.venueservice.dto.OfferUpdateRequestDTO;
import io.kk.bandhub.venueservice.service.OfferService;
import io.kk.bandhub.venueservice.service.VenueService;
import io.kk.bandhub.venueservice.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/offer")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;
    private final VenueService venueService;

    @PostMapping
    ResponseEntity<OfferDTO> createOffer(@RequestBody @Valid OfferRequestDTO offerDTO, Authentication authentication) {
        SecurityUtils.verifyAuthorities(authentication, venueService.findById(offerDTO.getVenueId()).getOwnerId());
        var result = offerService.createOffer(offerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping
    ResponseEntity<OfferDTO> updateOffer(@RequestBody @Valid OfferUpdateRequestDTO offerDTO, Authentication authentication) {
        SecurityUtils.verifyAuthorities(authentication, venueService.findById(offerDTO.getVenueId()).getOwnerId());
        var result = offerService.updateOffer(offerDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<OfferDTO> deleteOffer(@PathVariable Integer id, Authentication authentication) {
        SecurityUtils.verifyAuthorities(authentication, venueService.findById(offerService.getOfferById(id).getVenueId()).getOwnerId());
        offerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/user/{id}")
    ResponseEntity<List<OfferDTO>> getUserOffers(@PathVariable Integer id) {
        var result = offerService.getUserOffers(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<OfferDTO> getOffer(@PathVariable Integer id) {
        var result = offerService.getOfferById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/all")
    ResponseEntity<List<OfferDTO>> getAllOffers() {
        var result = offerService.getAllOffers();
        return ResponseEntity.ok(result);
    }
}
