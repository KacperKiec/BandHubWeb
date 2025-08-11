package io.kk.bandhub.eventservice.service;

import io.kk.bandhub.eventservice.dto.EventDTO;
import io.kk.bandhub.eventservice.strategy.EventSearchStrategy;
import io.kk.bandhub.eventservice.utils.SearchParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class EventSearchContext {

    private final Map<String, EventSearchStrategy> strategies;

    public EventSearchContext(List<EventSearchStrategy> strategyList) {
        strategies = strategyList.stream()
                .collect(Collectors.toMap(s -> s.getClass().getSimpleName(), Function.identity()));
    }

    public Page<EventDTO> search(SearchParams params, Pageable pageable) {
        boolean hasDate = params.getDate() != null;
        boolean hasBand = params.getBandName() != null && !params.getBandName().isBlank();
        boolean hasCities = params.getCities() != null && !params.getCities().isEmpty();

        if (hasDate) {
            if (hasBand) {
                if (hasCities) {
                    return strategies.get("DateBandCitySearchStrategy").search(params, pageable);
                } else {
                    return strategies.get("DateBandSearchStrategy").search(params, pageable);
                }
            } else {
                if (hasCities) {
                    return strategies.get("DateCitySearchStrategy").search(params, pageable);
                } else {
                    return strategies.get("DateSearchStrategy").search(params, pageable);
                }
            }
        } else {
            if (hasBand) {
                if (hasCities) {
                    return strategies.get("BandCitySearchStrategy").search(params, pageable);
                } else {
                    return strategies.get("BandSearchStrategy").search(params, pageable);
                }
            } else {
                if (hasCities) {
                    return strategies.get("CitySearchStrategy").search(params, pageable);
                } else {
                    return strategies.get("EmptySearchStrategy").search(params, pageable);
                }
            }
        }
    }

}
