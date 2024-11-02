package com.example.identity.service.impl;

import com.example.identity.dto.request.RoleRequest;
import com.example.identity.dto.response.RoleResponse;
import com.example.identity.entity.Permission;
import com.example.identity.entity.Role;
import com.example.identity.mapper.RoleMapper;
import com.example.identity.repository.PermissionRepository;
import com.example.identity.repository.RoleRepository;
import com.example.identity.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    @Override
    public RoleResponse create(RoleRequest request) {
        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());

        Role role = roleMapper.mapToEntity(request);
        role.setPermissions(new HashSet<>(permissions));
        role = roleRepository.save(role);

        return roleMapper.mapToResponse(role);
    }

    @Override
    public List<RoleResponse> getAll() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::mapToResponse)
                .toList();
    }

    @Override
    public void delete(String name) {
        roleRepository.deleteById(name);
    }
}
