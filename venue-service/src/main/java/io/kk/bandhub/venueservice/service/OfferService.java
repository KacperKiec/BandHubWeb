package io.kk.bandhub.venueservice.service;

import io.kk.bandhub.venueservice.dto.Mapper;
import io.kk.bandhub.venueservice.dto.OfferDTO;
import io.kk.bandhub.venueservice.exception.offer.OfferError;
import io.kk.bandhub.venueservice.exception.offer.OfferException;
import io.kk.bandhub.venueservice.exception.venue.VenueError;
import io.kk.bandhub.venueservice.exception.venue.VenueException;
import io.kk.bandhub.venueservice.model.Offer;
import io.kk.bandhub.venueservice.model.OfferPurpose;
import io.kk.bandhub.venueservice.model.OfferStatus;
import io.kk.bandhub.venueservice.repository.offer.OfferRepository;
import io.kk.bandhub.venueservice.repository.offer.OfferSpecification;
import io.kk.bandhub.venueservice.repository.venue.VenueRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final VenueRepository venueRepository;
    private final Mapper mapper;

    public OfferService(OfferRepository offerRepository, VenueRepository venueRepository, Mapper mapper) {
        this.offerRepository = offerRepository;
        this.venueRepository = venueRepository;
        this.mapper = mapper;
    }

    @Transactional
    public OfferDTO save(OfferDTO offerDTO) {
        Offer offer = new Offer();
        return setOfferFields(offerDTO, offer);
    }

    @Transactional
    public OfferDTO update(OfferDTO offerDTO) {
        var offer = offerRepository.findById(offerDTO.getId()).orElseThrow(
                () -> new OfferException(OfferError.OFFER_NOT_FOUND)
        );

        return setOfferFields(offerDTO, offer);
    }

    private OfferDTO setOfferFields(OfferDTO offerDTO, Offer offer) {
        offer.setDate(offerDTO.getDate());
        offer.setStatus(offerDTO.getStatus());
        offer.setEndTime(offerDTO.getEndTime());
        offer.setStartTime(offerDTO.getStartTime());
        offer.setPricePerHour(BigDecimal.valueOf(offerDTO.getPricePerHour()));
        offer.setPurpose(offerDTO.getPurpose());

        var venue = venueRepository.findById(offerDTO.getVenueId()).orElseThrow(
                () -> new VenueException(VenueError.VENUE_NOT_FOUND)
        );

        offer.setVenue(venue);
        return mapper.toOfferDTO(offerRepository.save(offer));
    }

    public void delete(Integer offerId) {
        var offer = offerRepository.findById(offerId).orElseThrow(
                () -> new OfferException(OfferError.OFFER_NOT_FOUND)
        );
        offerRepository.deleteById(offerId);
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
            OfferPurpose purpose,
            LocalDate date,
            Pageable pageable) {

        Specification<Offer> spec = OfferSpecification.hasCity(cities)
                .and(OfferSpecification.hasDate(date))
                .and(OfferSpecification.hasPurpose(purpose))
                .and(OfferSpecification.hasStatus(status));

        return offerRepository.findAll(spec, pageable).map(mapper::toOfferDTO);

    }
}
