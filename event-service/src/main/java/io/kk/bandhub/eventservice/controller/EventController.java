package io.kk.bandhub.eventservice.controller;

import io.kk.bandhub.eventservice.dto.EventRequestDTO;
import io.kk.bandhub.eventservice.dto.EventUpdateRequestDTO;
import io.kk.bandhub.eventservice.dto.FollowRequestDTO;
import io.kk.bandhub.eventservice.dto.UserEventDTO;
import io.kk.bandhub.eventservice.service.EventService;
import io.kk.bandhub.eventservice.service.UserEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final UserEventService userEventService;

    @PostMapping(value = "")
    ResponseEntity<UserEventDTO> createEvent(@RequestBody @Valid EventRequestDTO eventDTO) {
        var result = eventService.createEvent(eventDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping
    ResponseEntity<UserEventDTO> updateEvent(@RequestBody @Valid EventUpdateRequestDTO eventDTO) {
        var result = eventService.updateEvent(eventDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> deleteEvent(@PathVariable Integer id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/user/{id}")
    ResponseEntity<List<UserEventDTO>> getUserEvents(@PathVariable Integer id) {
        var result = userEventService.getUserEvents(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/follow")
    ResponseEntity<UserEventDTO> followEvent(@RequestBody FollowRequestDTO followRequest) {
        var result = userEventService.followEvent(followRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PatchMapping(value = "/unfollow")
    ResponseEntity<Void> unfollowEvent(@RequestBody FollowRequestDTO followRequest) {
        userEventService.unfollowEvent(followRequest);
        return ResponseEntity.noContent().build();
    }

}
