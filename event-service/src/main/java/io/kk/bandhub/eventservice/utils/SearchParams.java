package io.kk.bandhub.eventservice.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SearchParams {
    private LocalDate date;
    private String bandName;
    private List<String> cities;
}
