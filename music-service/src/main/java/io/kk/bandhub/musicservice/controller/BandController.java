package io.kk.bandhub.musicservice.controller;

import io.kk.bandhub.musicservice.dto.BandDTO;
import io.kk.bandhub.musicservice.dto.BandEvtDTO;
import io.kk.bandhub.musicservice.model.Genre;
import io.kk.bandhub.musicservice.service.BandService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * Controller class provides communication with client
 * */
@Controller
@RequestMapping(value = "/band")
public class BandController {
    private final BandService service;

    public BandController(BandService service) {
        this.service = service;
    }

    @PostMapping
    ResponseEntity<BandDTO> addBand(@RequestBody @Valid BandDTO bandDTO) {
        var savedBand = service.save(bandDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBand);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<BandDTO> getBandById(@PathVariable int id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping(value = "/evt/{id}")
    ResponseEntity<BandEvtDTO> getBandEventById(@PathVariable Integer id) {
        var result = service.getBandEvtById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/all")
    ResponseEntity<Page<BandDTO>> getAllBands(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @GetMapping(value = "/recommended")
    ResponseEntity<Page<BandDTO>> getRecommendedBands(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String userCity,
            @RequestParam(required = false) List<String> userGenres) {

        return ResponseEntity.ok(service.findBandByFilters(null, Collections.singletonList(userCity), null, userGenres, true, pageable));
    }

    @GetMapping(value = "/search")
    ResponseEntity<Page<BandDTO>> searchBand(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<String> cities,
            @RequestParam(required = false) Integer founderId,
            @RequestParam(required = false) List<String> genres,
            @RequestParam(required = false) Boolean isLookingMembers) {

        var bands = service.findBandByFilters(name, cities, founderId, genres, isLookingMembers, pageable);
        return ResponseEntity.ok(bands);
    }

    @PutMapping()
    ResponseEntity<BandDTO> updateBand(@RequestBody @Valid BandDTO bandDTO) {
        return ResponseEntity.ok(service.updateBand(bandDTO));
    }

    @DeleteMapping()
    ResponseEntity<?> deleteBand(@RequestBody BandDTO bandDTO) {
        service.deleteBand(bandDTO.getId());
        return ResponseEntity.noContent().build();
    }
}
