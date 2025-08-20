package io.kk.bandhub.musicservice.controller;

import io.kk.bandhub.musicservice.dto.BandDTO;
import io.kk.bandhub.musicservice.dto.BandMembershipRequestDTO;
import io.kk.bandhub.musicservice.dto.BandRequestDTO;
import io.kk.bandhub.musicservice.dto.BandUpdateRequestDTO;
import io.kk.bandhub.musicservice.service.BandService;
import io.kk.bandhub.musicservice.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping()
    ResponseEntity<BandDTO> updateBand(@RequestBody @Valid BandUpdateRequestDTO bandUpdateRequestDTO, Authentication authentication) {
        Integer founderId = bandUpdateRequestDTO.getFounderId();

        SecurityUtils.verifyAuthorities(authentication, founderId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.updateBand(bandUpdateRequestDTO));
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
}
