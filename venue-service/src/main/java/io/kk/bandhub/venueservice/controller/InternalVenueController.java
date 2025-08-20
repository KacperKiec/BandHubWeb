package io.kk.bandhub.venueservice.controller;

import io.kk.bandhub.venueservice.dto.VenueDTO;
import io.kk.bandhub.venueservice.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/venue/internal")
@RestController
@RequiredArgsConstructor
public class InternalVenueController {

    private final VenueService venueService;

    @GetMapping(value = "/ids")
    ResponseEntity<List<VenueDTO>> getVenuesByIds(@RequestParam List<Integer> ids) {
        var result = venueService.getVenuesByIds(ids);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<VenueDTO> getVenue(@PathVariable int id) {
        return ResponseEntity.ok(venueService.findById(id));
    }
}
