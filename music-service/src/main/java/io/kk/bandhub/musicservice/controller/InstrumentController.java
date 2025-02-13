package io.kk.bandhub.musicservice.controller;

import io.kk.bandhub.musicservice.dto.InstrumentDTO;
import io.kk.bandhub.musicservice.service.InstrumentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/instrument")
public class InstrumentController {

    private final InstrumentService instrumentService;

    public InstrumentController(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    @PostMapping
    ResponseEntity<InstrumentDTO> addGenre(@RequestBody InstrumentDTO instrumentDTO) {
        var result = instrumentService.save(instrumentDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> deleteGenre(@PathVariable Integer id) {
        instrumentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    ResponseEntity<InstrumentDTO> updateGenre(@RequestBody InstrumentDTO instrumentDTO) {
        var result = instrumentService.update(instrumentDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    ResponseEntity<InstrumentDTO> getGenre(@PathVariable Integer id) {
        var result = instrumentService.getById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    ResponseEntity<List<InstrumentDTO>> getAllGenres() {
        var result = instrumentService.getAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/paged")
    ResponseEntity<Page<InstrumentDTO>> getAllGenres(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        var result = instrumentService.getAllInstruments(pageable);
        return ResponseEntity.ok(result);
    }
}
