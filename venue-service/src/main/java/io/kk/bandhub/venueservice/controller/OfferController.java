package io.kk.bandhub.venueservice.controller;

import io.kk.bandhub.venueservice.dto.OfferDTO;
import io.kk.bandhub.venueservice.dto.OfferRequestDTO;
import io.kk.bandhub.venueservice.dto.OfferUpdateRequestDTO;
import io.kk.bandhub.venueservice.utils.OfferStatus;
import io.kk.bandhub.venueservice.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping(value = "/offer")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping
    ResponseEntity<OfferDTO> createOffer(@RequestBody @Valid OfferRequestDTO offerDTO) {
        var result = offerService.createOffer(offerDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.getId())
                .toUri();

        return ResponseEntity.created(location).body(result);
    }

    @PutMapping
    ResponseEntity<OfferDTO> updateOffer(@RequestBody @Valid OfferUpdateRequestDTO offerDTO) {
        var result = offerService.updateOffer(offerDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<OfferDTO> deleteOffer(@PathVariable Integer id) {
        offerService.delete(id);
        return ResponseEntity.noContent().build();
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

    @GetMapping(value = "/search")
    ResponseEntity<Page<OfferDTO>> searchOffers(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) List<String> cities,
            @RequestParam(required = false) OfferStatus status,
            @RequestParam(required = false)LocalDate date) {

        var result = offerService.searchOffers(cities, status, date, pageable);
        return ResponseEntity.ok(result);
    }
}
