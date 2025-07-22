package io.kk.bandhub.musicservice.service;

import io.kk.bandhub.musicservice.dto.BandJoinRequestDTO;
import io.kk.bandhub.musicservice.dto.BandJoinUpdateRequestDTO;
import io.kk.bandhub.musicservice.dto.BandMembershipRequestDTO;

import java.util.List;

public interface RequestService {
    void createJoinRequest(BandMembershipRequestDTO joinRequestDTO);
    void updateRequest(BandJoinUpdateRequestDTO requestDTO);
    List<BandJoinRequestDTO> getBandJoinRequests(Integer id);
}
