package io.kk.bandhub.bandhubbackend.controller;

import io.kk.bandhub.bandhubbackend.model.MusicianDTO;
import io.kk.bandhub.bandhubbackend.service.MusicianService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/profile")
public class UserController {
    private final MusicianService service;

    public UserController(MusicianService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<MusicianDTO> getProfile(@PathVariable int id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<MusicianDTO> updateProfile(@PathVariable int id, @RequestBody MusicianDTO dto) {
        return ResponseEntity.ok(service.updateMusician(dto));
    }

}
