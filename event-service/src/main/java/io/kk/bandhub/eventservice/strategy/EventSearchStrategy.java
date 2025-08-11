package io.kk.bandhub.eventservice.strategy;

import io.kk.bandhub.eventservice.dto.EventDTO;
import io.kk.bandhub.eventservice.utils.SearchParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventSearchStrategy {
    Page<EventDTO> search(SearchParams params, Pageable pageable);
}
