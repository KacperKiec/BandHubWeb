package io.kk.bandhub.venueservice.service.impl;

import io.kk.bandhub.venueservice.dto.Mapper;
import io.kk.bandhub.venueservice.dto.OfferDTO;
import io.kk.bandhub.venueservice.dto.OfferRequestDTO;
import io.kk.bandhub.venueservice.dto.OfferUpdateRequestDTO;
import io.kk.bandhub.venueservice.exception.offer.OfferError;
import io.kk.bandhub.venueservice.exception.offer.OfferException;
import io.kk.bandhub.venueservice.exception.venue.VenueError;
import io.kk.bandhub.venueservice.exception.venue.VenueException;
import io.kk.bandhub.venueservice.model.Offer;
import io.kk.bandhub.venueservice.service.OfferAvailabilityService;
import io.kk.bandhub.venueservice.service.OfferService;
import io.kk.bandhub.venueservice.utils.OfferStatus;
import io.kk.bandhub.venueservice.repository.offer.OfferRepository;
import io.kk.bandhub.venueservice.repository.offer.OfferSpecification;
import io.kk.bandhub.venueservice.repository.venue.VenueRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final VenueRepository venueRepository;
    private final OfferAvailabilityService offerAvailabilityService;
    private final Mapper mapper;

    @Transactional
    public OfferDTO createOffer(OfferRequestDTO offerDTO) {
        Offer offer = new Offer();
        setOfferFields(offer, offerDTO.getDate(), offerDTO.getStatus(), offerDTO.getEndTime(), offerDTO.getStartTime(), offerDTO.getPricePerHour(), offerDTO.getVenueId());
        offerAvailabilityService.setAvailabilityRanges(offer);

        return mapper.toOfferDTO(offerRepository.save(offer));
    }

    @Transactional
    public OfferDTO updateOffer(OfferUpdateRequestDTO offerDTO) {
        var offer = offerRepository.findById(offerDTO.getId()).orElseThrow(
                () -> new OfferException(OfferError.OFFER_NOT_FOUND)
        );

        setOfferFields(offer, offerDTO.getDate(), offerDTO.getStatus(), offerDTO.getEndTime(), offerDTO.getStartTime(), offerDTO.getPricePerHour(), offerDTO.getVenueId());
        offerAvailabilityService.setAvailabilityRanges(offer);

        return mapper.toOfferDTO(offerRepository.save(offer));
    }

    private void setOfferFields(Offer offer, LocalDate date, OfferStatus status, LocalTime endTime, LocalTime startTime, Double pricePerHour, Integer venueId) {
        offer.setDate(date);
        offer.setStatus(status);
        offer.setEndTime(endTime);
        offer.setStartTime(startTime);
        offer.setPricePerHour(BigDecimal.valueOf(pricePerHour));

        var venue = venueRepository.findById(venueId).orElseThrow(
                () -> new VenueException(VenueError.VENUE_NOT_FOUND)
        );
        offer.setVenue(venue);
    }

    public void delete(Integer offerId) {
        offerRepository.findById(offerId).orElseThrow(
                () -> new OfferException(OfferError.OFFER_NOT_FOUND)
        );
        offerRepository.deleteById(offerId);
    }

    public List<OfferDTO> getUserOffers(Integer id) {
        return offerRepository.findByVenueOwnerId(id).stream()
                .map(mapper::toOfferDTO).toList();
    }

    public OfferDTO getOfferById(Integer offerId) {
        var offer = offerRepository.findById(offerId).orElseThrow(
                () -> new OfferException(OfferError.OFFER_NOT_FOUND)
        );
        return mapper.toOfferDTO(offer);
    }

    public List<OfferDTO> getAllOffers() {
        return offerRepository.findAll().stream().map(mapper::toOfferDTO).toList();
    }

    public Page<OfferDTO> searchOffers(
            List<String> cities,
            OfferStatus status,
            LocalDate date,
            Pageable pageable) {

        Specification<Offer> spec = OfferSpecification.hasCity(cities)
                .and(OfferSpecification.hasDate(date))
                .and(OfferSpecification.hasStatus(status));
        return offerRepository.findAll(spec, pageable).map(mapper::toOfferDTO);
    }
}
