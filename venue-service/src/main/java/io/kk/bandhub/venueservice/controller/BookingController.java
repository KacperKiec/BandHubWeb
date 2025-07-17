package io.kk.bandhub.venueservice.controller;

import io.kk.bandhub.venueservice.dto.BookingDTO;
import io.kk.bandhub.venueservice.dto.BookingRequestDTO;
import io.kk.bandhub.venueservice.utils.OfferStatus;
import io.kk.bandhub.venueservice.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    ResponseEntity<BookingDTO> createBooking(@RequestBody @Valid BookingRequestDTO bookingDTO) {
        var result = bookingService.createBooking(bookingDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> deleteBooking(@PathVariable Integer id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<BookingDTO> getBooking(@PathVariable Integer id) {
        var result = bookingService.getBooking(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/all")
    ResponseEntity<List<BookingDTO>> getAllBookings() {
        var result = bookingService.getAllBookings();
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/search")
    ResponseEntity<Page<BookingDTO>> getBookings(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) Integer offerId,
            @RequestParam(required = false) Integer tenantId,
            @RequestParam(required = false)OfferStatus status) {

        var result = bookingService.getBookings(pageable, offerId, tenantId, status);
        return ResponseEntity.ok(result);
    }
}
