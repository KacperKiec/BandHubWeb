package io.kk.bandhub.bandhubbackend.controller;

import io.kk.bandhub.bandhubbackend.model.Band;
import io.kk.bandhub.bandhubbackend.model.BandDTO;
import io.kk.bandhub.bandhubbackend.model.MusicianDTO;
import io.kk.bandhub.bandhubbackend.service.BandService;
import io.kk.bandhub.bandhubbackend.service.MusicianService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/profile")
public class UserController {
    private final MusicianService musicianService;
    private final BandService bandService;

    public UserController(MusicianService service, BandService bandService) {
        this.musicianService = service;
        this.bandService = bandService;
    }

    @GetMapping(value = "/{id}")
    //@PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<MusicianDTO> getProfile(@PathVariable int id) {
        return ResponseEntity.ok(musicianService.getById(id));
    }

    @PutMapping(value = "/{id}")
    //@PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<MusicianDTO> updateProfile(@PathVariable int id, @RequestBody MusicianDTO dto) {
        return ResponseEntity.ok(musicianService.updateMusician(dto));
    }

    @GetMapping(value = "/{id}/my-bands")
    //@PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<Page<BandDTO>> getMyBands(@RequestParam (defaultValue = "0") int page, @PathVariable int id) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(bandService.getUserBands(id, pageable));
    }

    @PostMapping(value = "/{id}/create-band")
    //@PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<BandDTO> createBand(@PathVariable int id, @RequestBody Band band) {
        var savedBand = bandService.save(band, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBand);
    }

}
