package io.kk.bandhub.eventservice.controller;

import io.kk.bandhub.eventservice.dto.EventDTO;
import io.kk.bandhub.eventservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/event/public")
@RestController
@RequiredArgsConstructor
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    ResponseEntity<List<EventDTO>> getAllEvents() {
        var result = eventService.getAllEvents();
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<EventDTO> getEventById(@PathVariable Integer id) {
        var result = eventService.getEventById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/search")
    ResponseEntity<Page<EventDTO>> searchEvents(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String bandName,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) List<String> cities) {

        var result = eventService.searchEvents(date, bandName, cities, pageable);
        return ResponseEntity.ok(result);
    }
}
