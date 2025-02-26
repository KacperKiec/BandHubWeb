package io.kk.bandhub.venueservice.service;

import io.kk.bandhub.venueservice.dto.Mapper;
import io.kk.bandhub.venueservice.dto.VenueDTO;
import io.kk.bandhub.venueservice.exception.VenueError;
import io.kk.bandhub.venueservice.exception.VenueException;
import io.kk.bandhub.venueservice.model.Venue;
import io.kk.bandhub.venueservice.repository.venue.VenueRepository;
import io.kk.bandhub.venueservice.repository.venue.VenueSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VenueService {

    private final VenueRepository venueRepository;
    private final Mapper mapper;

    public VenueService(VenueRepository venueRepository, Mapper mapper) {
        this.venueRepository = venueRepository;
        this.mapper = mapper;
    }

    @Transactional
    public VenueDTO save(VenueDTO dto) {
        var existingVenue = venueRepository.findByNameAndOwnerId(dto.getName(), dto.getOwnerId());
        if (existingVenue.isPresent()) {
            throw new VenueException(VenueError.VENUE_ALREADY_EXISTS);
        }

        Venue venue = new Venue();
        return setVenueFields(dto, venue);
    }

    @Transactional
    public VenueDTO update(VenueDTO venueDTO) {
        var existingVenue = venueRepository.findById(venueDTO.getId()).orElseThrow(
                () -> new VenueException(VenueError.VENUE_NOT_FOUND)
        );

        return setVenueFields(venueDTO, existingVenue);
    }

    private VenueDTO setVenueFields(VenueDTO venueDTO, Venue existingVenue) {
        existingVenue.setName(venueDTO.getName());
        existingVenue.setDescription(venueDTO.getDescription());
        existingVenue.setCapacity(venueDTO.getCapacity());
        existingVenue.setAddress(venueDTO.getAddress());
        existingVenue.setCity(venueDTO.getCity());
        existingVenue.setOwnerId(venueDTO.getOwnerId());
        return mapper.toVenueDTO(venueRepository.save(existingVenue));
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
        var existingVenue = venueRepository.findById(id).orElseThrow(
                () -> new VenueException(VenueError.VENUE_NOT_FOUND)
        );
        venueRepository.deleteById(id);
    }
}
