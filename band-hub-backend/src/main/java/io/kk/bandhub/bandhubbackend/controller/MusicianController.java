package io.kk.bandhub.bandhubbackend.controller;

import io.kk.bandhub.bandhubbackend.model.Instrument;
import io.kk.bandhub.bandhubbackend.model.MusicalGenre;
import io.kk.bandhub.bandhubbackend.model.Musician;
import io.kk.bandhub.bandhubbackend.model.MusicianDTO;
import io.kk.bandhub.bandhubbackend.service.MusicianService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    ResponseEntity<List<MusicianDTO>> getAllMusicians() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping(value = "/search")
    ResponseEntity<List<MusicianDTO>> searchMusician(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<String> cities,
            @RequestParam(required = false) List<MusicalGenre> genres,
            @RequestParam(required = false) List<Instrument> instruments,
            @RequestParam(required = false) Boolean isLookingForBand,
            @RequestParam(required = false) String sex) {

        List<MusicianDTO> musicians = service.findMusiciansByFilters(name, cities, genres, instruments, isLookingForBand, sex);
        return ResponseEntity.ok(musicians);
    }

}
