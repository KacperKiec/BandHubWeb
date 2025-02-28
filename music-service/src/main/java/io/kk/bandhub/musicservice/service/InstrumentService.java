package io.kk.bandhub.musicservice.service;

import io.kk.bandhub.musicservice.dto.InstrumentDTO;
import io.kk.bandhub.musicservice.dto.Mapper;
import io.kk.bandhub.musicservice.exception.instrument.InstrumentError;
import io.kk.bandhub.musicservice.exception.instrument.InstrumentException;
import io.kk.bandhub.musicservice.model.Instrument;
import io.kk.bandhub.musicservice.repository.instrument.InstrumentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstrumentService {

    private final InstrumentRepository instrumentRepository;
    private final Mapper mapper;

    public InstrumentService(InstrumentRepository instrumentRepository, Mapper mapper) {
        this.instrumentRepository = instrumentRepository;
        this.mapper = mapper;
    }

    public InstrumentDTO save(InstrumentDTO instrumentDTO) {
        var existingInstrument = instrumentRepository.findByName(instrumentDTO.getName());
        if (existingInstrument.isPresent()) {
            throw new InstrumentException(InstrumentError.INSTRUMENT_ALREADY_EXISTS);
        }

        Instrument instrument = new Instrument();
        instrument.setName(instrumentDTO.getName());

        return mapper.toInstrumentDTO(instrumentRepository.save(instrument));
    }

    public void delete(Integer id) {
        var existingInstrument = instrumentRepository.findById(id).orElseThrow(
                () -> new InstrumentException(InstrumentError.INSTRUMENT_NOT_FOUND)
        );
        instrumentRepository.deleteById(id);
    }

    public InstrumentDTO update(InstrumentDTO instrumentDTO) {
        var existingInstrument = instrumentRepository.findByName(instrumentDTO.getName()).orElseThrow(
                () -> new InstrumentException(InstrumentError.INSTRUMENT_NOT_FOUND)
        );
        existingInstrument.setName(instrumentDTO.getName());
        return mapper.toInstrumentDTO(instrumentRepository.save(existingInstrument));
    }

    public InstrumentDTO getById(Integer id) {
        var existingInstrument = instrumentRepository.findById(id).orElseThrow(
                () -> new InstrumentException(InstrumentError.INSTRUMENT_NOT_FOUND)
        );
        return mapper.toInstrumentDTO(existingInstrument);
    }

    public List<InstrumentDTO> getAll() {
        return instrumentRepository.findAll().stream().map(mapper::toInstrumentDTO).toList();
    }

    public Page<InstrumentDTO> getAllInstruments(Pageable pageable) {
        return instrumentRepository.findAll(pageable).map(mapper::toInstrumentDTO);
    }
}
