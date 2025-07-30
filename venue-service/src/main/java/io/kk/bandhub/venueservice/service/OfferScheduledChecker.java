package io.kk.bandhub.venueservice.service;

import io.kk.bandhub.venueservice.repository.offer.OfferRepository;
import io.kk.bandhub.venueservice.utils.OfferStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OfferScheduledChecker {
    private final OfferRepository offerRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void changeOfferStatus() {
        var offers = offerRepository.findAll();

        offers.forEach(offer -> {
            if (offer.getDate().isBefore(LocalDate.now())) {
                offer.setStatus(OfferStatus.ENDED);
                offer.getAvailabilityRanges().clear();
                offerRepository.save(offer);
            }
        });
    }
}
