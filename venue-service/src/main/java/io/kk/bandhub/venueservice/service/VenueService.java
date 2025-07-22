package io.kk.bandhub.venueservice.service;

import io.kk.bandhub.venueservice.dto.VenueDTO;
import io.kk.bandhub.venueservice.dto.VenueRequestDTO;
import io.kk.bandhub.venueservice.dto.VenueUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VenueService {
    VenueDTO createVenue(VenueRequestDTO dto);
    VenueDTO update(VenueUpdateRequestDTO dto);
    List<VenueDTO> getUserVenues(Integer ownerId);
    VenueDTO findById(Integer id);
    List<VenueDTO> findAll();
    Page<VenueDTO> findAll(Pageable pageable);
    Page<VenueDTO> findAll(Pageable pageable, String name, Integer maxCapacity, Integer minCapacity, Integer ownerId, List<String> cities);
    void delete(Integer id);
}
