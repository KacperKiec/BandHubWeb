package io.kk.bandhub.musicservice.service;

import io.kk.bandhub.musicservice.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BandService {
    BandDTO save(BandRequestDTO bandDTO);
    BandDTO getById(Integer id);
    BandEventDataDTO getBandEvtById(Integer id);
    Page<BandDTO> getAll(Pageable pageable);
    Page<BandDTO> findBandByFilters(String name, List<String> cities, Integer founderId, List<String> genres, Boolean isLookingMembers, Pageable pageable);
    BandDTO updateBand(BandUpdateRequestDTO bandDTO);
    void addMusicianToBand(BandMembershipRequestDTO dto);
    void removeMusicianFromBand(BandMembershipRequestDTO dto);
    void leaveBand(BandMembershipRequestDTO dto);
    void deleteBand(Integer bandId);
}
