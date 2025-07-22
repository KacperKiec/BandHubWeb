package io.kk.bandhub.userservice.controller;

import io.kk.bandhub.userservice.dto.RoleDTO;
import io.kk.bandhub.userservice.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<RoleDTO> addRole(@RequestBody @Valid RoleDTO roleDTO) {
        var result = roleService.save(roleDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<RoleDTO> updateRole(@RequestBody @Valid RoleDTO roleDTO) {
        var result = roleService.update(roleDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<RoleDTO> getRole(@PathVariable Integer id) {
        var result = roleService.findById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }

}
