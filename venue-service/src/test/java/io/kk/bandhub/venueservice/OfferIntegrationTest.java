package io.kk.bandhub.venueservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kk.bandhub.venueservice.dto.OfferRequestDTO;
import io.kk.bandhub.venueservice.dto.OfferUpdateRequestDTO;
import io.kk.bandhub.venueservice.dto.OfferDTO;
import io.kk.bandhub.venueservice.model.Offer;
import io.kk.bandhub.venueservice.model.Venue;
import io.kk.bandhub.venueservice.repository.offer.OfferRepository;
import io.kk.bandhub.venueservice.repository.venue.VenueRepository;
import io.kk.bandhub.venueservice.service.OfferAvailabilityService;
import io.kk.bandhub.venueservice.dto.Mapper;
import io.kk.bandhub.venueservice.utils.OfferStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class OfferIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Mock
    private OfferAvailabilityService offerAvailabilityService;

    @Mock
    private Mapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Venue venue;

    @BeforeEach
    void setupSecurityContext() {
        Jwt jwt = Jwt.withTokenValue("test-token")
                .header("alg", "none")
                .claim("userId", 99L)
                .claim("roles", List.of("ROLE_USER"))
                .build();

        JwtAuthenticationToken authToken =
                new JwtAuthenticationToken(jwt, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    @BeforeEach
    void setup() {
        venueRepository.deleteAll();
        offerRepository.deleteAll();

        venue = new Venue();
        venue.setName("Test Venue");
        venue.setOwnerId(99);
        venue.setLatitude(10.0);
        venue.setLongitude(20.0);
        venue.setCapacity(100);
        venue.setCity("City");
        venue.setPostalCode("11-111");
        venue.setCountry("Poland");
        venue.setStreet("Street");
        venue.setNumber("21");
        venueRepository.save(venue);

        Mockito.when(offerAvailabilityService.isPossibleToCreate(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(true);
        Mockito.when(offerAvailabilityService.setAvailabilityRanges(Mockito.any()))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
    }

    // -------------------------------------------------------------------------
    // CREATE OFFER
    // -------------------------------------------------------------------------

    @Test
    @WithMockUser(username = "testUser")
    void createOffer_success() throws Exception {

        OfferRequestDTO dto = new OfferRequestDTO();
        dto.setDate(LocalDate.now().plusDays(1));
        dto.setStartTime(LocalTime.of(10, 0));
        dto.setEndTime(LocalTime.of(12, 0));
        dto.setPricePerHour(BigDecimal.valueOf(150));
        dto.setVenueId(venue.getId());
        dto.setStatus(OfferStatus.ACTIVE);

        // mock mapper
        Mockito.when(mapper.toOfferDTO(Mockito.any(Offer.class)))
                .then(invocation -> {
                    Offer o = invocation.getArgument(0);
                    OfferDTO d = new OfferDTO();
                    d.setId(o.getId());
                    d.setVenueId(o.getVenue().getId());
                    d.setDate(o.getDate());
                    d.setStartTime(o.getStartTime());
                    d.setEndTime(o.getEndTime());
                    return d;
                });

        mockMvc.perform(post("/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.venueId").value(venue.getId()));

        assertThat(offerRepository.findAll()).hasSize(1);
    }

    // -------------------------------------------------------------------------
    // UPDATE OFFER
    // -------------------------------------------------------------------------
    @Test
    @WithMockUser(username = "testUser")
    void updateOffer_success() throws Exception {

        // create initial offer
        Offer offer = new Offer();
        offer.setDate(LocalDate.now().plusDays(1));
        offer.setStartTime(LocalTime.of(10, 0));
        offer.setEndTime(LocalTime.of(12, 0));
        offer.setVenue(venue);
        offer.setStatus(OfferStatus.ACTIVE);
        offer.setPricePerHour(BigDecimal.valueOf(100));
        offerRepository.save(offer);

        OfferUpdateRequestDTO dto = new OfferUpdateRequestDTO();
        dto.setId(offer.getId());
        dto.setDate(offer.getDate());
        dto.setStartTime(LocalTime.of(14, 0));
        dto.setEndTime(LocalTime.of(16, 0));
        dto.setVenueId(venue.getId());
        dto.setPricePerHour(BigDecimal.valueOf(200));
        dto.setStatus(OfferStatus.ACTIVE);

        Mockito.when(mapper.toOfferDTO(Mockito.any(Offer.class)))
                .then(invocation -> {
                    Offer o = invocation.getArgument(0);
                    OfferDTO d = new OfferDTO();
                    d.setId(o.getId());
                    d.setStartTime(o.getStartTime());
                    return d;
                });

        mockMvc.perform(put("/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(offer.getId()))
                .andExpect(jsonPath("$.startTime").value("14:00:00"));

        Offer updated = offerRepository.findById(offer.getId()).orElseThrow();
        assertThat(updated.getStartTime()).isEqualTo(LocalTime.of(14, 0));
    }

    // -------------------------------------------------------------------------
    // DELETE OFFER
    // -------------------------------------------------------------------------
    @Test
    @WithMockUser(username = "testUser")
    void deleteOffer_success() throws Exception {

        Offer offer = new Offer();
        offer.setDate(LocalDate.now().plusDays(1));
        offer.setStartTime(LocalTime.of(10, 0));
        offer.setEndTime(LocalTime.of(12, 0));
        offer.setVenue(venue);
        offer.setStatus(OfferStatus.ACTIVE);
        offer.setPricePerHour(BigDecimal.valueOf(120));
        offerRepository.save(offer);

        mockMvc.perform(delete("/offer/" + offer.getId()))
                .andExpect(status().isNoContent());

        assertThat(offerRepository.findAll()).isEmpty();
    }

    // -------------------------------------------------------------------------
    // GET OFFER BY ID
    // -------------------------------------------------------------------------
    @Test
    @WithMockUser
    void getOffer_success() throws Exception {

        Offer offer = new Offer();
        offer.setDate(LocalDate.now().plusDays(1));
        offer.setStartTime(LocalTime.of(10, 0));
        offer.setEndTime(LocalTime.of(12, 0));
        offer.setVenue(venue);
        offer.setStatus(OfferStatus.ACTIVE);
        offer.setPricePerHour(BigDecimal.valueOf(120));
        offerRepository.save(offer);

        Mockito.when(mapper.toOfferDTO(Mockito.any()))
                .then(invocation -> {
                    Offer o = invocation.getArgument(0);
                    OfferDTO d = new OfferDTO();
                    d.setId(o.getId());
                    return d;
                });

        mockMvc.perform(get("/offer/" + offer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(offer.getId()));
    }
}
