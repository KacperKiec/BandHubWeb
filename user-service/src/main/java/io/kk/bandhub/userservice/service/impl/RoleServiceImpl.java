package io.kk.bandhub.userservice.service.impl;

import io.kk.bandhub.userservice.dto.Mapper;
import io.kk.bandhub.userservice.dto.RoleDTO;
import io.kk.bandhub.userservice.exception.RoleException;
import io.kk.bandhub.userservice.exception.RoleError;
import io.kk.bandhub.userservice.model.Role;
import io.kk.bandhub.userservice.repository.RoleRepository;
import io.kk.bandhub.userservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final Mapper mapper;

    public RoleDTO save(RoleDTO roleDTO) {
        var existingRole = roleRepository.findRoleByName(roleDTO.getName());
        if (existingRole.isPresent()) {
            throw new RoleException(RoleError.ROLE_ALREADY_EXISTS);
        }

        Role role = new Role();
        role.setName(roleDTO.getName());

        return mapper.toRoleDTO(roleRepository.save(role));
    }

    public void delete(Integer id) {
        var existingRole = roleRepository.findRoleById(id).orElseThrow(
                () -> new RoleException(RoleError.ROLE_NOT_FOUND)
        );

        roleRepository.deleteById(existingRole.getId());
    }

    public RoleDTO update(RoleDTO roleDTO) {
        var existingRole = roleRepository.findRoleById(roleDTO.getId()).orElseThrow(
                () -> new RoleException(RoleError.ROLE_NOT_FOUND)
        );

        existingRole.setName(roleDTO.getName());
        return mapper.toRoleDTO(roleRepository.save(existingRole));
    }

    public RoleDTO findById(Integer id) {
        var role = roleRepository.findRoleById(id).orElseThrow(
                () -> new RoleException(RoleError.ROLE_NOT_FOUND)
        );
        return mapper.toRoleDTO(role);
    }

    public List<RoleDTO> findAll() {
        return roleRepository.findAll().stream().map(mapper::toRoleDTO).toList();
    }

}
