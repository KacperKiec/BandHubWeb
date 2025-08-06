package io.kk.bandhub.venueservice.controller;

import io.kk.bandhub.venueservice.dto.BookingDTO;
import io.kk.bandhub.venueservice.dto.BookingRequestDTO;
import io.kk.bandhub.venueservice.service.BookingService;
import io.kk.bandhub.venueservice.utils.OfferStatus;
import io.kk.bandhub.venueservice.utils.SecurityUtils;
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

@Controller
@RequestMapping(value = "/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    ResponseEntity<BookingDTO> createBooking(@RequestBody @Valid BookingRequestDTO bookingDTO, Authentication authentication) {
        SecurityUtils.verifyAuthorities(authentication, bookingDTO.getTenantId());
        var result = bookingService.createBooking(bookingDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> deleteBooking(@PathVariable Integer id, @RequestParam String initiator, Authentication authentication) {
        SecurityUtils.verifyAuthorities(authentication, bookingService.getBooking(id).getTenantId());
        bookingService.deleteBooking(id, initiator);
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

    @GetMapping(value = "/user/{id}/usable")
    ResponseEntity<List<BookingDTO>> getUserUsableBookings(@PathVariable Integer id, Authentication authentication) {
        SecurityUtils.verifyAuthorities(authentication, id);
        var result = bookingService.getUsableUserBookings(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/user/{id}")
    ResponseEntity<Page<BookingDTO>> getUserBookings(
            @PathVariable Integer id,
            @PageableDefault(size = 10, sort = "Offer.date", direction = Sort.Direction.DESC) Pageable pageable,
            Authentication authentication) {
        SecurityUtils.verifyAuthorities(authentication, id);
        var result = bookingService.getUserBookings(id, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/owner/{id}")
    ResponseEntity<Page<BookingDTO>> getOwnerBookings(
            @PathVariable Integer id,
            @PageableDefault(size = 10, sort = "Offer.date", direction = Sort.Direction.DESC) Pageable pageable,
            Authentication authentication) {
        SecurityUtils.verifyAuthorities(authentication, id);
        var result = bookingService.getOwnerBookings(id, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/search")
    ResponseEntity<Page<BookingDTO>> getBookings(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) Integer offerId,
            @RequestParam(required = false) Integer tenantId,
            @RequestParam(required = false) OfferStatus status) {

        var result = bookingService.getBookings(pageable, offerId, tenantId, status);
        return ResponseEntity.ok(result);
    }
}
