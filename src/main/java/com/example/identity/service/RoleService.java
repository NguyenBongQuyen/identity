package com.example.identity.service;

import com.example.identity.dto.request.RoleRequest;
import com.example.identity.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse create(RoleRequest request);

    List<RoleResponse> getAll();

    void delete(String name);
}
