package io.kk.bandhub.musicservice.controller;

import io.kk.bandhub.musicservice.dto.BandDTO;
import io.kk.bandhub.musicservice.service.BandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/band/public")
@RestController
@RequiredArgsConstructor
public class PublicBandController {

    private final BandService bandService;

    @GetMapping(value = "/{id}")
    ResponseEntity<BandDTO> getBandById(@PathVariable int id) {
        return ResponseEntity.ok(bandService.getById(id));
    }

    @GetMapping(value = "/all")
    ResponseEntity<Page<BandDTO>> getAllBands(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(bandService.getAll(pageable));
    }

    @GetMapping(value = "/search")
    ResponseEntity<Page<BandDTO>> searchBand(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<String> cities,
            @RequestParam(required = false) Integer founderId,
            @RequestParam(required = false) List<String> genres,
            @RequestParam(required = false) Boolean isLookingMembers) {

        var bands = bandService.findBandByFilters(name, cities, founderId, genres, isLookingMembers, pageable);
        return ResponseEntity.ok(bands);
    }
}
