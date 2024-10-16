package com.example.identity.service;

import com.example.identity.dto.request.UserRequest;
import com.example.identity.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse create(UserRequest request);

    List<UserResponse> getUsers();

    UserResponse getUser(String id);

    UserResponse update(String id, UserRequest request);

    void delete(String id);
}
