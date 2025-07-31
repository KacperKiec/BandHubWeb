package io.kk.bandhub.venueservice.service;

import io.kk.bandhub.venueservice.repository.offer.OfferRepository;
import io.kk.bandhub.venueservice.utils.OfferStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OfferScheduledChecker {

    private final OfferRepository offerRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void changeOfferStatusScheduled() {
        checkAndUpdateOffers();
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void changeOfferStatusOnStartup() {
        checkAndUpdateOffers();
    }

    private void checkAndUpdateOffers() {
        var offers = offerRepository.findAll();
        var today = LocalDate.now();

        offers.forEach(offer -> {
            if (offer.getDate().isBefore(today)) {
                offer.setStatus(OfferStatus.ENDED);
                offer.getAvailabilityRanges().clear();
                offerRepository.save(offer);
            }
        });
    }
}
