package io.kk.bandhub.bandhubbackend.controller;

import io.kk.bandhub.bandhubbackend.model.Instrument;
import io.kk.bandhub.bandhubbackend.model.MusicalGenre;
import io.kk.bandhub.bandhubbackend.model.Musician;
import io.kk.bandhub.bandhubbackend.model.MusicianDTO;
import io.kk.bandhub.bandhubbackend.service.MusicianService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class provides communication with client
 * */
@Controller
@RequestMapping(value = "/musician")
public class MusicianController {
    private final MusicianService service;

    public MusicianController(final MusicianService service) {
        this.service = service;
    }

    @PostMapping
    ResponseEntity<MusicianDTO> addMusician(@RequestBody Musician musician) {
        var savedMusician = service.save(musician);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMusician);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<MusicianDTO> getMusicianById(@PathVariable int id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping(value = "/all")
    ResponseEntity<Page<MusicianDTO>> getAllMusicians(@RequestParam (defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @GetMapping(value = "/search")
    ResponseEntity<Page<MusicianDTO>> searchMusician(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<String> cities,
            @RequestParam(required = false) List<MusicalGenre> genres,
            @RequestParam(required = false) List<Instrument> instruments,
            @RequestParam(required = false) Boolean isLookingForBand,
            @RequestParam(required = false) String sex) {

        Pageable pageable = PageRequest.of(page, 10);
        var musicians = service.findMusiciansByFilters(name, cities, genres, instruments, isLookingForBand, sex, pageable);
        return ResponseEntity.ok(musicians);
    }

}
