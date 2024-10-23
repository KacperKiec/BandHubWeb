package io.kk.bandhub.bandhubbackend.controller;

import io.kk.bandhub.bandhubbackend.model.Band;
import io.kk.bandhub.bandhubbackend.model.BandDTO;
import io.kk.bandhub.bandhubbackend.model.Instrument;
import io.kk.bandhub.bandhubbackend.model.MusicalGenre;
import io.kk.bandhub.bandhubbackend.service.BandService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    ResponseEntity<BandDTO> addBand(@RequestBody Band band) {
        var savedBand = service.save(band);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBand);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<BandDTO> getBandById(@PathVariable int id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping(value = "/all")
    ResponseEntity<Page<BandDTO>> getAllBands(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @GetMapping(value = "/recommended")
    ResponseEntity<Page<BandDTO>> getRecommendedBands(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String userCity,
            @RequestParam(required = false) List<MusicalGenre> userGenres,
            @RequestParam(required = false) List<Instrument> userInstruments) {
        Pageable pageable = PageRequest.of(page, 10);

        return ResponseEntity.ok(service.findBandByFilters(null, Collections.singletonList(userCity), null, userGenres, userInstruments, null, true, pageable));
    }

    @GetMapping(value = "/search")
    ResponseEntity<Page<BandDTO>> searchBand(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<String> cities,
            @RequestParam(required = false) Integer founderId,
            @RequestParam(required = false) List<MusicalGenre> genres,
            @RequestParam(required = false) List<Instrument> requestedInstruments,
            @RequestParam(required = false) List<Instrument> currentInstruments,
            @RequestParam(required = false) Boolean isLookingMembers) {

        Pageable pageable = PageRequest.of(page, 10);
        var bands = service.findBandByFilters(name, cities, founderId, genres, requestedInstruments, currentInstruments, isLookingMembers, pageable);
        return ResponseEntity.ok(bands);
    }
}
