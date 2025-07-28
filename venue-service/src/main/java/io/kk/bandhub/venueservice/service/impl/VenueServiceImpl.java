package io.kk.bandhub.venueservice.service.impl;

import io.kk.bandhub.venueservice.dto.Mapper;
import io.kk.bandhub.venueservice.dto.VenueDTO;
import io.kk.bandhub.venueservice.dto.VenueRequestDTO;
import io.kk.bandhub.venueservice.dto.VenueUpdateRequestDTO;
import io.kk.bandhub.venueservice.exception.venue.VenueError;
import io.kk.bandhub.venueservice.exception.venue.VenueException;
import io.kk.bandhub.venueservice.model.Venue;
import io.kk.bandhub.venueservice.repository.venue.VenueRepository;
import io.kk.bandhub.venueservice.repository.venue.VenueSpecification;
import io.kk.bandhub.venueservice.service.VenueService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;
    private final Mapper mapper;

    @Transactional
    public VenueDTO createVenue(VenueRequestDTO dto) {
        var existingVenue = venueRepository.findByNameAndOwnerId(dto.getName(), dto.getOwnerId());
        if (existingVenue.isPresent()) {
            throw new VenueException(VenueError.VENUE_ALREADY_EXISTS);
        }

        Venue venue = new Venue();
        setVenueFields(venue, dto.getName(), dto.getDescription(), dto.getCapacity(), dto.getOwnerId(), dto.getCity(), dto.getCountry(), dto.getStreet(), dto.getNumber(), dto.getPostalCode(), dto.getLatitude(), dto.getLongitude());

        var mappedVenue = mapper.toVenueDTOReg(venue);
        venueRepository.save(venue);
        mappedVenue.setId(venue.getId());

        return mappedVenue;
    }

    @Transactional
    public VenueDTO update(VenueUpdateRequestDTO dto) {
        var existingVenue = venueRepository.findById(dto.getId()).orElseThrow(
                () -> new VenueException(VenueError.VENUE_NOT_FOUND)
        );

        setVenueFields(existingVenue, dto.getName(), dto.getDescription(), dto.getCapacity(), dto.getOwnerId(), dto.getCity(), dto.getCountry(), dto.getStreet(), dto.getNumber(), dto.getPostalCode(), dto.getLatitude(), dto.getLongitude());

        venueRepository.save(existingVenue);

        return mapper.toVenueDTO(existingVenue);
    }

    private void setVenueFields(Venue venue, String name, String description, Integer capacity, Integer ownerId, String city, String country, String street, String number, String postalCode, Double latitude, Double longitude) {
        venue.setName(name);
        venue.setDescription(description);
        venue.setCapacity(capacity);
        venue.setOwnerId(ownerId);
        venue.setCity(city);
        venue.setCountry(country);
        venue.setStreet(street);
        venue.setNumber(number);
        venue.setPostalCode(postalCode);
        venue.setLatitude(latitude);
        venue.setLongitude(longitude);
    }

    public List<VenueDTO> getUserVenues(Integer ownerId) {
        return venueRepository.findByOwnerId(ownerId)
                .stream().map(mapper::toVenueDTO).toList();
    }

    public List<VenueDTO> getVenuesByIds(List<Integer> ids) {
        return venueRepository.findVenuesByIdIsIn(ids).stream()
                .map(mapper::toVenueDTO).toList();
    }

    public VenueDTO findById(Integer id) {
        return mapper.toVenueDTO(venueRepository.findById(id).orElseThrow(
                () -> new VenueException(VenueError.VENUE_NOT_FOUND)
        ));
    }

    public List<VenueDTO> findAll() {
        return venueRepository.findAll().stream().map(mapper::toVenueDTO).toList();
    }

    public Page<VenueDTO> findAll(Pageable pageable) {
        return venueRepository.findAll(pageable).map(mapper::toVenueDTO);
    }

    public Page<VenueDTO> findAll(
            Pageable pageable,
            String name,
            Integer maxCapacity,
            Integer minCapacity,
            Integer ownerId,
            List<String> cities) {

        Specification<Venue> spec = VenueSpecification.hasCities(cities)
                .and(VenueSpecification.hasName(name))
                .and(VenueSpecification.hasOwnerId(ownerId))
                .and(VenueSpecification.hasLessCapacity(maxCapacity))
                .and(VenueSpecification.hasMoreCapacity(minCapacity));

        return venueRepository.findAll(spec, pageable).map(mapper::toVenueDTO);
    }

    public void delete(Integer id) {
        venueRepository.findById(id).orElseThrow(
                () -> new VenueException(VenueError.VENUE_NOT_FOUND)
        );
        venueRepository.deleteById(id);
    }
}
