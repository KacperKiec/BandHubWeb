package io.kk.bandhub.venueservice.service;

import io.kk.bandhub.venueservice.dto.OfferDTO;
import io.kk.bandhub.venueservice.dto.OfferRequestDTO;
import io.kk.bandhub.venueservice.dto.OfferUpdateRequestDTO;
import io.kk.bandhub.venueservice.utils.OfferStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface OfferService {
    OfferDTO createOffer(OfferRequestDTO offerDTO);
    OfferDTO updateOffer(OfferUpdateRequestDTO offerDTO);
    void delete(Integer offerId);
    List<OfferDTO> getUserOffers(Integer id);
    OfferDTO getOfferById(Integer offerId);
    List<OfferDTO> getAllOffers();
    Page<OfferDTO> searchOffers(List<String> cities, OfferStatus status, LocalDate date, Double minLatitude, Double maxLatitude, Double minLongitude, Double maxLongitude, Pageable pageable);
}
