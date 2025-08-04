package io.kk.bandhub.eventservice;

import io.kk.bandhub.eventservice.dto.*;
import io.kk.bandhub.eventservice.integration.MusicServiceClient;
import io.kk.bandhub.eventservice.integration.VenueServiceClient;
import io.kk.bandhub.eventservice.model.Event;
import io.kk.bandhub.eventservice.repository.EventRepository;
import io.kk.bandhub.eventservice.service.impl.EventServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private VenueServiceClient venueServiceClient;

    @Mock
    private MusicServiceClient musicServiceClient;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void shouldCreateEventFromBooking() {
        // given
        EventRequestDTO request = new EventRequestDTO();
        request.setBandId(1);
        request.setCreatorId(10);
        request.setVenueId(100);
        request.setBookingId(200);
        request.setDate(LocalDate.now().plusDays(5));
        request.setStartTime(LocalTime.of(18, 0));
        request.setEndTime(LocalTime.of(20, 0));
        request.setDescription("Koncert testowy");
        request.setTicketPrice(20.0);

        BookingEventDataDTO data = new BookingEventDataDTO();
        data.setVenueId(100);
        data.setVenueName("Test Venue");
        data.setTenantId(10);
        data.setStartTime(LocalTime.of(18, 0));
        data.setEndTime(LocalTime.of(21, 0));

        data.setStreet("Testowa");
        data.setNumber("1A");
        data.setCity("Miasto");
        data.setCountry("PL");
        data.setPostalCode("00-000");

        BandEventDataDTO band = new BandEventDataDTO();
        band.setId(1);
        band.setName("Test Band");
        band.setFounderId(10);

        // when
        eventService.createEvent(request);

        // then
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void shouldCreateEventByVenueOwner() {
        // given
        EventRequestDTO request = new EventRequestDTO();
        request.setBandId(1);
        request.setCreatorId(30);
        request.setVenueId(100);
        request.setDate(LocalDate.now().plusDays(3));
        request.setStartTime(LocalTime.of(19, 0));
        request.setEndTime(LocalTime.of(21, 0));
        request.setDescription("Wydarzenie właściciela");
        request.setTicketPrice(15.0);

        VenueDTO venueData = VenueDTO.builder()
                .id(100)
                .name("Owner Venue")
                .ownerId(30)
                .street("Główna")
                .number("12")
                .city("Miasto")
                .country("PL")
                .postalCode("12-121")
                .build();

        BandEventDataDTO band = new BandEventDataDTO();
        band.setId(1);
        band.setName("Test Band");
        band.setFounderId(30);

        // when
        eventService.createEvent(request);

        // then
        verify(eventRepository).save(any(Event.class));
    }

}
