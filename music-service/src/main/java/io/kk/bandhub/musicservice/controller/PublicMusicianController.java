package io.kk.bandhub.musicservice.controller;

import io.kk.bandhub.musicservice.dto.BandDTO;
import io.kk.bandhub.musicservice.dto.MusicianDTO;
import io.kk.bandhub.musicservice.service.MusicianService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/musician/public")
@RestController
@RequiredArgsConstructor
public class PublicMusicianController {

    private final MusicianService musicianService;

    @GetMapping(value = "/{id}")
    ResponseEntity<MusicianDTO> getMusicianById(@PathVariable int id) {
        return ResponseEntity.ok(musicianService.getById(id));
    }

    @GetMapping(value = "/all")
    ResponseEntity<Page<MusicianDTO>> getAllMusicians(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(musicianService.getAll(pageable));
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

        var musicians = musicianService.findMusiciansByFilters(genres, instruments, isLookingForBand, pageable, firstName, lastName, minAge, maxAge, cities);
        return ResponseEntity.ok(musicians);
    }

    @GetMapping(value = "/{id}/bands")
    ResponseEntity<List<BandDTO>> getMusicianBands(@PathVariable int id) {
        return ResponseEntity.ok(musicianService.getMusicianBands(id));
    }
}
