package com.example.identity.service.impl;

import com.example.identity.constant.ErrorCode;
import com.example.identity.dto.request.UserRequest;
import com.example.identity.dto.response.UserResponse;
import com.example.identity.entity.User;
import com.example.identity.exception.AppException;
import com.example.identity.mapper.UserMapper;
import com.example.identity.repository.UserRepository;
import com.example.identity.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    @Override
    public UserResponse create(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.mapToEntity(request);
        user = userRepository.save(user);
        return userMapper.mapToResponse(user);
    }

    @Override
    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::mapToResponse).toList();
    }

    @Override
    public UserResponse getUser(String id) {
         User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
         return userMapper.mapToResponse(user);
    }

    @Override
    public UserResponse update(String id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setId(id);
        userMapper.updateUser(user, request);
        user = userRepository.save(user);
        return userMapper.mapToResponse(user);
    }

    @Override
    public void delete(String id) {
        userRepository.deleteById(id);
    }
}
