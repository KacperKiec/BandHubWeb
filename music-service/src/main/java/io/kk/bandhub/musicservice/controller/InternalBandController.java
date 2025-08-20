package io.kk.bandhub.musicservice.controller;

import io.kk.bandhub.musicservice.dto.BandEventDataDTO;
import io.kk.bandhub.musicservice.dto.MusicianDTO;
import io.kk.bandhub.musicservice.service.BandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/band/internal")
@RestController
@RequiredArgsConstructor
public class InternalBandController {

    private final BandService bandService;

    @GetMapping(value = "/evt/{id}")
    ResponseEntity<BandEventDataDTO> getBandEventById(@PathVariable Integer id) {
        var result = bandService.getBandEvtById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/evt/name")
    ResponseEntity<BandEventDataDTO> getBandEventByName(@RequestParam String name) {
        var result = bandService.getBandEvtByName(name);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{id}/members")
    ResponseEntity<List<Integer>> getBandMembersIds(@PathVariable Integer id) {
        var result = bandService.getById(id).getMusicians().stream().map(MusicianDTO::getId).toList();
        return ResponseEntity.ok(result);
    }
}
