package io.kk.bandhub.musicservice.controller;

import io.kk.bandhub.musicservice.dto.*;
import io.kk.bandhub.musicservice.service.BandService;
import io.kk.bandhub.musicservice.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class provides communication with client
 * */
@Controller
@RequestMapping(value = "/band")
@RequiredArgsConstructor
public class BandController {
    private final BandService service;

    @PostMapping
    ResponseEntity<BandDTO> addBand(@RequestBody @Valid BandRequestDTO bandRequestDTO) {
        var savedBand = service.save(bandRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBand);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<BandDTO> getBandById(@PathVariable int id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping(value = "/evt/{id}")
    ResponseEntity<BandEventDataDTO> getBandEventById(@PathVariable Integer id) {
        var result = service.getBandEvtById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/all")
    ResponseEntity<Page<BandDTO>> getAllBands(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @GetMapping(value = "/search")
    ResponseEntity<Page<BandDTO>> searchBand(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<String> cities,
            @RequestParam(required = false) Integer founderId,
            @RequestParam(required = false) List<String> genres,
            @RequestParam(required = false) Boolean isLookingMembers) {

        var bands = service.findBandByFilters(name, cities, founderId, genres, isLookingMembers, pageable);
        return ResponseEntity.ok(bands);
    }

    @PutMapping()
    ResponseEntity<BandDTO> updateBand(@RequestBody @Valid BandUpdateRequestDTO bandUpdateRequestDTO, Authentication authentication) {
        Integer founderId = bandUpdateRequestDTO.getFounderId();

        SecurityUtils.verifyAuthorities(authentication, founderId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.updateBand(bandUpdateRequestDTO));
    }

    @PatchMapping(value = "/add-member")
    ResponseEntity<Void> addMember(@RequestBody @Valid BandMembershipRequestDTO bandMembershipRequestDTO, Authentication authentication) {
        Integer founderId = service.getById(bandMembershipRequestDTO.getBandId()).getFounderId();

        SecurityUtils.verifyAuthorities(authentication, founderId);

        service.addMusicianToBand(bandMembershipRequestDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PatchMapping(value = "/remove-member")
    ResponseEntity<Void> removeMember(@RequestBody @Valid BandMembershipRequestDTO bandMembershipRequestDTO, Authentication authentication) {
        Integer founderId = service.getById(bandMembershipRequestDTO.getBandId()).getFounderId();

        SecurityUtils.verifyAuthorities(authentication, founderId);

        service.removeMusicianFromBand(bandMembershipRequestDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PatchMapping(value = "/leave")
    ResponseEntity<Void> leaveBand(@RequestBody @Valid BandMembershipRequestDTO bandMembershipRequestDTO, Authentication authentication) {
        SecurityUtils.verifyAuthorities(authentication, bandMembershipRequestDTO.getMusicianId());
        service.leaveBand(bandMembershipRequestDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping(value = "/{bandId}")
    ResponseEntity<Void> deleteBand(@PathVariable Integer bandId, Authentication authentication) {
        SecurityUtils.verifyAuthorities(authentication, service.getById(bandId).getFounderId());
        service.deleteBand(bandId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}/members")
    ResponseEntity<List<Integer>> getBandMembersIds(@PathVariable Integer id) {
        var result = service.getById(id).getMusicians().stream().map(MusicianDTO::getId).toList();
        return ResponseEntity.ok(result);
    }
}
