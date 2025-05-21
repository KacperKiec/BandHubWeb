package io.kk.bandhub.musicservice.controller;

import io.kk.bandhub.musicservice.dto.BandDTO;
import io.kk.bandhub.musicservice.dto.MusicianDTO;
import io.kk.bandhub.musicservice.dto.MusicianRequestDTO;
import io.kk.bandhub.musicservice.service.MusicianService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    ResponseEntity<MusicianDTO> addMusician(@RequestBody @Valid MusicianRequestDTO musicianRequestDTO) {
        var savedMusician = service.save(musicianRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMusician);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<MusicianDTO> getMusicianById(@PathVariable int id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping(value = "/{id}/bands")
    ResponseEntity<List<BandDTO>> getMusicianBands(@PathVariable int id) {
        return ResponseEntity.ok(service.getMusicianBands(id));
    }

    @GetMapping(value = "/all")
    ResponseEntity<Page<MusicianDTO>> getAllMusicians(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @GetMapping(value = "/search")
    ResponseEntity<Page<MusicianDTO>> searchMusician(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) List<String> cities,
            @RequestParam(required = false) List<String> genres,
            @RequestParam(required = false) List<String> instruments,
            @RequestParam(required = false) Boolean isLookingForBand) {

        var musicians = service.findMusiciansByFilters(genres, instruments, isLookingForBand, pageable, firstName, lastName, minAge, maxAge, cities);
        return ResponseEntity.ok(musicians);
    }

    @PutMapping()
    ResponseEntity<Void> updateMusician(@RequestBody @Valid MusicianRequestDTO musicianRequestDTO) {
        service.updateMusician(musicianRequestDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> deleteMusician(@PathVariable Integer id) {
        service.deleteMusician(id);
        return ResponseEntity.noContent().build();
    }

}
