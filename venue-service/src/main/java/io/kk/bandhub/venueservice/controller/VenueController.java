package io.kk.bandhub.venueservice.controller;

import io.kk.bandhub.venueservice.dto.VenueDTO;
import io.kk.bandhub.venueservice.service.VenueService;
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
import java.util.List;

@Controller
@RequestMapping(value = "/venue")
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @PostMapping
    ResponseEntity<VenueDTO> createVenue(@RequestBody @Valid VenueDTO venueDTO) {
        var result = venueService.save(venueDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.getId())
                .toUri();

        return ResponseEntity.created(location).body(result);
    }

    @PutMapping
    ResponseEntity<VenueDTO> updateVenue(@RequestBody @Valid VenueDTO venueDTO) {
        var result = venueService.update(venueDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> deleteVenue(@PathVariable int id) {
        venueService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<VenueDTO> getVenue(@PathVariable int id) {
        return ResponseEntity.ok(venueService.findById(id));
    }

    @GetMapping(value = "/all")
    ResponseEntity<List<VenueDTO>> getAllVenues() {
        return ResponseEntity.ok(venueService.findAll());
    }

    @GetMapping(value = "/search")
    ResponseEntity<Page<VenueDTO>> searchVenues(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer maxCapacity,
            @RequestParam(required = false) Integer minCapacity,
            @RequestParam(required = false) Integer ownerId,
            @RequestParam(required = false) List<String> cities) {

        var result = venueService.findAll(pageable, name, maxCapacity, minCapacity, ownerId, cities);
        return ResponseEntity.ok(result);
    }
}
