package io.kk.bandhub.eventservice.controller;

import io.kk.bandhub.eventservice.dto.EventDTO;
import io.kk.bandhub.eventservice.service.EventService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping(value = "/event")
public class EventController {

    private final EventService eventService;
    private final RabbitTemplate rabbitTemplate;

    public EventController(EventService eventService, RabbitTemplate rabbitTemplate) {
        this.eventService = eventService;
        this.rabbitTemplate = rabbitTemplate;
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
    ResponseEntity<Page<EventDTO>> searchEvents(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<String> cities,
            @RequestParam(required = false) Integer organizerId,
            @RequestParam(required = false)LocalDate date){

        var result = eventService.searchEvents(pageable, cities, name, date, organizerId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> deleteEvent(@PathVariable Integer id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @RabbitListener(queues = "event-queue")
    public void createEvent(EventDTO eventDTO) {
        eventService.createEvent(eventDTO);
    }
}
