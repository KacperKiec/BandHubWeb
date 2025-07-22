package io.kk.bandhub.userservice.service;

import io.kk.bandhub.userservice.dto.RoleDTO;

import java.util.List;

public interface RoleService {
    RoleDTO save(RoleDTO roleDTO);
    void delete(Integer id);
    RoleDTO update(RoleDTO roleDTO);
    RoleDTO findById(Integer id);
    List<RoleDTO> findAll();
}
