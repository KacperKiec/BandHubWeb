package io.kk.bandhub.musicservice.controller;

import io.kk.bandhub.musicservice.dto.MusicianDTO;
import io.kk.bandhub.musicservice.model.Genre;
import io.kk.bandhub.musicservice.model.Instrument;
import io.kk.bandhub.musicservice.service.MusicianService;
import jakarta.validation.Valid;
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
    ResponseEntity<MusicianDTO> addMusician(@RequestBody @Valid MusicianDTO musicianDTO) {
        var savedMusician = service.save(musicianDTO);
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
            @RequestParam(required = false) List<String> cities,
            @RequestParam(required = false) List<Genre> genres,
            @RequestParam(required = false) List<Instrument> instruments,
            @RequestParam(required = false) Boolean isLookingForBand) {

        Pageable pageable = PageRequest.of(page, 10);
        var musicians = service.findMusiciansByFilters(cities, genres, instruments, isLookingForBand, pageable);
        return ResponseEntity.ok(musicians);
    }

    @PutMapping()
    ResponseEntity<MusicianDTO> updateMusician(@RequestBody @Valid MusicianDTO musicianDTO) {
        return ResponseEntity.ok(service.updateMusician(musicianDTO));
    }

}
