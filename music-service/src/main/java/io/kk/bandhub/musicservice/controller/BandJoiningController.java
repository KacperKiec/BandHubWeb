package io.kk.bandhub.musicservice.controller;

import io.kk.bandhub.musicservice.dto.*;
import io.kk.bandhub.musicservice.service.BandService;
import io.kk.bandhub.musicservice.service.InvitationService;
import io.kk.bandhub.musicservice.service.RequestService;
import io.kk.bandhub.musicservice.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/band/joining")
@RequiredArgsConstructor
public class BandJoiningController {
    private final RequestService requestService;
    private final InvitationService invitationService;
    private final BandService bandService;

    @PostMapping(value = "/invite")
    ResponseEntity<Void> createInvitation(@RequestBody @Valid BandMembershipRequestDTO dto, Authentication auth) {
        Integer founderId = bandService.getById(dto.getBandId()).getFounderId();

        SecurityUtils.verifyAuthorities(auth, founderId);
        invitationService.createInvitation(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/request")
    ResponseEntity<Void> createRequest(@RequestBody @Valid BandMembershipRequestDTO dto, Authentication auth) {
        SecurityUtils.verifyAuthorities(auth, dto.getMusicianId());
        requestService.createJoinRequest(dto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping(value = "/invitation")
    ResponseEntity<Void> respondInvitation(@RequestBody @Valid BandInvitationUpdateRequestDTO dto, Authentication auth) {
        SecurityUtils.verifyAuthorities(auth, dto.getInvitedMusicianId());
        invitationService.updateInvitation(dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping(value = "/request")
    ResponseEntity<Void> respondRequest(@RequestBody @Valid BandJoinUpdateRequestDTO dto, Authentication auth) {
        Integer founderId = bandService.getById(dto.getBandId()).getFounderId();

        SecurityUtils.verifyAuthorities(auth, founderId);
        requestService.updateRequest(dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(value = "/user/invitations")
    ResponseEntity<List<BandInvitationDTO>> getUserInvitations(Authentication auth) {
        var uid = SecurityUtils.getUserIdFromAuthentication(auth);
        return ResponseEntity.ok(invitationService.getMusicianInvitations(uid.intValue()));
    }

    @GetMapping(value = "/{id}/requests")
    ResponseEntity<List<BandJoinRequestDTO>> getBandRequests(@PathVariable Integer id) {
        return ResponseEntity.ok(requestService.getBandJoinRequests(id));
    }
}
