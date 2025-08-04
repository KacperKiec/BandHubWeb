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
import io.kk.bandhub.venueservice.repository.booking.BookingRepository;
import io.kk.bandhub.venueservice.repository.offer.OfferRepository;
import io.kk.bandhub.venueservice.repository.offer.OfferSpecification;
import io.kk.bandhub.venueservice.repository.venue.VenueRepository;
import io.kk.bandhub.venueservice.service.OfferAvailabilityService;
import io.kk.bandhub.venueservice.service.OfferService;
import io.kk.bandhub.venueservice.utils.OfferStatus;
import io.kk.bandhub.venueservice.utils.TimeRange;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final VenueRepository venueRepository;
    private final OfferAvailabilityService offerAvailabilityService;
    private final BookingRepository bookingRepository;
    private final Mapper mapper;

    @Transactional
    public OfferDTO createOffer(OfferRequestDTO offerDTO) {
        Offer offer = new Offer();
        setOfferFields(offer, offerDTO.getDate(), offerDTO.getStatus(), offerDTO.getEndTime(), offerDTO.getStartTime(), offerDTO.getPricePerHour(), offerDTO.getVenueId());

        if (!offerAvailabilityService.isPossibleToCreate(new TimeRange(offer.getStartTime(), offer.getEndTime()), offer.getDate(), offer.getVenue().getId(), null)) {
            throw new OfferException(OfferError.OFFER_ALREADY_EXISTS);
        }
        offerAvailabilityService.setAvailabilityRanges(offer);

        return mapper.toOfferDTO(offerRepository.save(offer));
    }

    @Transactional
    public OfferDTO updateOffer(OfferUpdateRequestDTO offerDTO) {
        var offer = offerRepository.findById(offerDTO.getId()).orElseThrow(
                () -> new OfferException(OfferError.OFFER_NOT_FOUND)
        );

        setOfferFields(offer, offerDTO.getDate(), offerDTO.getStatus(), offerDTO.getEndTime(), offerDTO.getStartTime(), offerDTO.getPricePerHour(), offerDTO.getVenueId());

        if (!offerAvailabilityService.isPossibleToCreate(new TimeRange(offer.getStartTime(), offer.getEndTime()), offer.getDate(), offer.getVenue().getId(), offer.getId())) {
            throw new OfferException(OfferError.OFFER_ALREADY_EXISTS);
        }

        offerAvailabilityService.setAvailabilityRanges(offer);

        if (!offerAvailabilityService.checkChangingPossibility(offer)) {
            throw new OfferException(OfferError.CANNOT_EDIT_OFFER);
        }

        return mapper.toOfferDTO(offerRepository.save(offer));
    }

    private void setOfferFields(Offer offer, LocalDate date, OfferStatus status, LocalTime endTime, LocalTime startTime, BigDecimal pricePerHour, Integer venueId) {
        offer.setDate(date);
        offer.setStatus(status);
        offer.setEndTime(endTime);
        offer.setStartTime(startTime);
        offer.setPricePerHour(pricePerHour);

        var venue = venueRepository.findById(venueId).orElseThrow(
                () -> new VenueException(VenueError.VENUE_NOT_FOUND)
        );
        offer.setVenue(venue);
    }

    public void delete(Integer offerId) {
        offerRepository.findById(offerId).orElseThrow(
                () -> new OfferException(OfferError.OFFER_NOT_FOUND)
        );

        var bookings = bookingRepository.findAllByOfferId(offerId);

        if (!bookings.isEmpty()) {
            throw new OfferException(OfferError.CANNOT_DELETE_OFFER);
        }

        offerRepository.deleteById(offerId);
    }

    public List<OfferDTO> getUserOffers(Integer id) {
        return offerRepository.findByVenueOwnerId(id).stream()
                .map(mapper::toOfferDTO)
                .sorted(Comparator.comparing(OfferDTO::getDate)
                .thenComparing(OfferDTO::getStartTime))
                .toList();

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

    public List<OfferDTO> getNotCanceledOffersByVenueAndDate(Integer venueId, LocalDate date) {
        return offerRepository.findOffersByDateAndVenueIdAndStatusIn(date, venueId, List.of(OfferStatus.ACTIVE, OfferStatus.CLAIMED))
                .stream().map(mapper::toOfferDTO).toList();
    }

    public List<OfferDTO> searchOffers(
            List<String> cities,
            OfferStatus status,
            LocalDate date,
            Double minLatitude,
            Double maxLatitude,
            Double minLongitude,
            Double maxLongitude) {

        Specification<Offer> spec = OfferSpecification.hasCity(cities)
                .and(OfferSpecification.hasDate(date))
                .and(OfferSpecification.hasStatus(status))
                .and(OfferSpecification.hasCoordinates(minLatitude, maxLatitude, minLongitude, maxLongitude));
        return offerRepository.findAll(spec).stream().map(offer -> {
            var dto = mapper.toOfferDTO(offer);
            dto.setLatitude(offer.getVenue().getLatitude());
            dto.setLongitude(offer.getVenue().getLongitude());
            dto.setCapacity(offer.getVenue().getCapacity());
            return dto;
        }).toList();
    }
}
