package io.kk.bandhub.musicservice.controller;

import io.kk.bandhub.musicservice.dto.MusicianDTO;
import io.kk.bandhub.musicservice.dto.MusicianRequestDTO;
import io.kk.bandhub.musicservice.service.MusicianService;
import io.kk.bandhub.musicservice.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class provides communication with client
 * */
@Controller
@RequestMapping(value = "/musician")
@AllArgsConstructor
public class MusicianController {
    private final MusicianService service;

    @PostMapping
    ResponseEntity<MusicianDTO> addMusician(@RequestBody @Valid MusicianRequestDTO musicianRequestDTO) {
        var savedMusician = service.save(musicianRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMusician);
    }

    @PutMapping()
    ResponseEntity<Void> updateMusician(@RequestBody @Valid MusicianRequestDTO musicianRequestDTO, Authentication authentication) {

        SecurityUtils.verifyAuthorities(authentication, musicianRequestDTO.getId());

        service.updateMusician(musicianRequestDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> deleteMusician(@PathVariable Integer id) {
        service.deleteMusician(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/me")
    ResponseEntity<MusicianDTO> getMusicianByUserId(Authentication authentication) {
        var uid = SecurityUtils.getUserIdFromAuthentication(authentication);
        return ResponseEntity.ok(service.getById(uid.intValue()));
    }
}
