package io.kk.bandhub.venueservice.controller;

import io.kk.bandhub.venueservice.dto.VenueDTO;
import io.kk.bandhub.venueservice.dto.VenueRequestDTO;
import io.kk.bandhub.venueservice.dto.VenueUpdateRequestDTO;
import io.kk.bandhub.venueservice.exception.venue.VenueError;
import io.kk.bandhub.venueservice.exception.venue.VenueException;
import io.kk.bandhub.venueservice.service.VenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/venue")
@RequiredArgsConstructor
public class VenueController {

    private final VenueService venueService;

    @PostMapping
    ResponseEntity<VenueDTO> createVenue(@RequestBody @Valid VenueRequestDTO venueRequestDTO, Authentication authentication) {
        verifyAuthorities(authentication, venueRequestDTO.getOwnerId());
        var result = venueService.createVenue(venueRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping
    ResponseEntity<VenueDTO> updateVenue(@RequestBody @Valid VenueUpdateRequestDTO venueUpdateRequestDTO, Authentication authentication) {
        verifyAuthorities(authentication, venueUpdateRequestDTO.getOwnerId());
        var result = venueService.update(venueUpdateRequestDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
    }

    @GetMapping(value = "/user/{id}")
    ResponseEntity<List<VenueDTO>> getUserVenues(@PathVariable Integer id) {
        var result = venueService.getUserVenues(id);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> deleteVenue(@PathVariable int id, Authentication authentication) {
        verifyAuthorities(authentication, venueService.findById(id).getOwnerId());
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

    private void verifyAuthorities(Authentication authentication, Integer userId) {
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
        Jwt jwt = jwtAuth.getToken();
        Long userIdFromToken = jwt.getClaim("userId");

        if (!userIdFromToken.equals(userId.longValue())) {
            throw new VenueException(VenueError.ACCESS_DENIED);
        }
    }
}
