package io.kk.bandhub.bandhubbackend.controller;

import io.kk.bandhub.bandhubbackend.model.Band;
import io.kk.bandhub.bandhubbackend.model.BandDTO;
import io.kk.bandhub.bandhubbackend.model.Instrument;
import io.kk.bandhub.bandhubbackend.model.MusicalGenre;
import io.kk.bandhub.bandhubbackend.service.BandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    ResponseEntity<List<BandDTO>> getAllBands() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping(value = "/search")
    ResponseEntity<List<BandDTO>> searchBand(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<String> cities,
            @RequestParam(required = false) Integer founderId,
            @RequestParam(required = false) List<MusicalGenre> genres,
            @RequestParam(required = false) List<Instrument> requestedInstruments,
            @RequestParam(required = false) List<Instrument> currentInstruments,
            @RequestParam(required = false) Boolean isLookingMembers) {

        List<BandDTO> bands = service.findBandByFilters(name, cities, founderId, genres, requestedInstruments, currentInstruments, isLookingMembers);
        return ResponseEntity.ok(bands);
    }
}
