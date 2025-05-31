package io.kk.bandhub.musicservice.controller;

import io.kk.bandhub.musicservice.dto.BandInvitationDTO;
import io.kk.bandhub.musicservice.dto.BandJoinRequestDTO;
import io.kk.bandhub.musicservice.dto.BandMembershipRequestDTO;
import io.kk.bandhub.musicservice.exception.musician.MusicianError;
import io.kk.bandhub.musicservice.exception.musician.MusicianException;
import io.kk.bandhub.musicservice.service.BandJoiningService;
import io.kk.bandhub.musicservice.service.BandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/band-joining")
@RequiredArgsConstructor
public class BandJoiningController {
    private final BandJoiningService bandJoiningService;
    private final BandService bandService;

    @PostMapping(value = "/invite")
    ResponseEntity<Void> createInvitation(@RequestBody @Valid BandMembershipRequestDTO dto, Authentication auth) {
        Integer founderId = bandService.getById(dto.getBandId()).getFounderId();

        verifyAuthorities(auth, founderId);
        bandJoiningService.createInvitation(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/request")
    ResponseEntity<Void> createRequest(@RequestBody @Valid BandMembershipRequestDTO dto, Authentication auth) {
        verifyAuthorities(auth, dto.getMusicianId());
        bandJoiningService.createJoinRequest(dto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping(value = "/invitation")
    ResponseEntity<Void> updateInvitation(@RequestBody BandInvitationDTO dto, Authentication auth) {
        verifyAuthorities(auth, dto.getInvitedMusicianId());
        bandJoiningService.updateInvitation(dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping(value = "/request")
    ResponseEntity<Void> updateRequest(@RequestBody BandJoinRequestDTO dto, Authentication auth) {
        Integer founderId = bandService.getById(dto.getBandId()).getFounderId();

        verifyAuthorities(auth, founderId);
        bandJoiningService.updateRequest(dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(value = "/user/invitations")
    ResponseEntity<List<BandInvitationDTO>> getUserInvitations(Authentication auth) {
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) auth;
        Jwt jwt = jwtAuth.getToken();
        Long userIdFromToken = jwt.getClaim("userId");
        return ResponseEntity.ok(bandJoiningService.getMusicianInvitations(userIdFromToken.intValue()));
    }

    @GetMapping(value = "/band/{id}/requests")
    ResponseEntity<List<BandJoinRequestDTO>> getBandRequests(@PathVariable Integer id, Authentication auth) {
        return ResponseEntity.ok(bandJoiningService.getBandJoinRequests(id));
    }

    private void verifyAuthorities(Authentication authentication, Integer userId) {
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
        Jwt jwt = jwtAuth.getToken();
        Long userIdFromToken = jwt.getClaim("userId");

        if (!userIdFromToken.equals(userId.longValue())) {
            throw new MusicianException(MusicianError.ACCESS_DENIED);
        }
    }
}
