package io.kk.bandhub.bandhubbackend.controller;

import io.kk.bandhub.bandhubbackend.model.Musician;
import io.kk.bandhub.bandhubbackend.service.MusicianService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "musician")
public class MusicianController {
    private final MusicianService service;

    public MusicianController(final MusicianService service) {
        this.service = service;
    }

    @PostMapping
    ResponseEntity<Musician> addMusician(@RequestBody Musician musician) {
        return ResponseEntity.ok(service.save(musician));
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<Musician> getMusicianById(@PathVariable int id) {
        return ResponseEntity.ok(service.getById(id));
    }
}
