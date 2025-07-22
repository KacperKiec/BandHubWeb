package io.kk.bandhub.musicservice.service;

import io.kk.bandhub.musicservice.dto.InstrumentDTO;

import java.util.List;

public interface InstrumentService {
    InstrumentDTO save(InstrumentDTO instrumentDTO);
    void delete(Integer id);
    InstrumentDTO update(InstrumentDTO instrumentDTO);
    InstrumentDTO getById(Integer id);
    List<InstrumentDTO> getAll();
}
