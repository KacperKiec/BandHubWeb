package io.kk.bandhub.musicservice.controller;

import io.kk.bandhub.musicservice.dto.GenreDTO;
import io.kk.bandhub.musicservice.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/genre")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @PostMapping
    ResponseEntity<GenreDTO> addGenre(@RequestBody GenreDTO genreDTO) {
        var result = genreService.save(genreDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> deleteGenre(@PathVariable Integer id) {
        genreService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    ResponseEntity<GenreDTO> updateGenre(@RequestBody GenreDTO genreDTO) {
        var result = genreService.update(genreDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    ResponseEntity<GenreDTO> getGenre(@PathVariable Integer id) {
        var result = genreService.getGenreById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    ResponseEntity<List<GenreDTO>> getAllGenres() {
        var result = genreService.getAllGenres();
        return ResponseEntity.ok(result);
    }
}
