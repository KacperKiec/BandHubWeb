package io.kk.bandhub.eventservice.controller;

import io.kk.bandhub.eventservice.dto.EventDTO;
import io.kk.bandhub.eventservice.dto.EventRequestDTO;
import io.kk.bandhub.eventservice.dto.EventUpdateRequestDTO;
import io.kk.bandhub.eventservice.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping(value = "/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    ResponseEntity<Void> createEvent(@RequestBody @Valid EventRequestDTO eventDTO) {
        eventService.createEvent(eventDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    ResponseEntity<EventDTO> updateEvent(@RequestBody @Valid EventUpdateRequestDTO eventDTO) {
        var result = eventService.updateEvent(eventDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/all")
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
    ResponseEntity<List<EventDTO>> searchEvents(
            @RequestParam(required = false) Integer bandId,
            @RequestParam(required = false)LocalDate date,
            @RequestParam(required = false)List<String> cities){

        var result = eventService.searchEvents(date, bandId, cities);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> deleteEvent(@PathVariable Integer id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

}
