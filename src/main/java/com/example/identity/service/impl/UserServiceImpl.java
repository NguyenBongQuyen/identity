package com.example.identity.service.impl;

import com.example.identity.constant.ErrorCode;
import com.example.identity.constant.enums.RoleEnum;
import com.example.identity.dto.request.UserRequest;
import com.example.identity.dto.response.UserResponse;
import com.example.identity.entity.Role;
import com.example.identity.entity.User;
import com.example.identity.exception.AppException;
import com.example.identity.mapper.UserMapper;
import com.example.identity.repository.RoleRepository;
import com.example.identity.repository.UserRepository;
import com.example.identity.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public UserResponse create(UserRequest request) {
        User user = userMapper.mapToEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(RoleEnum.USER.name()).ifPresent(roles::add);
        user.setRoles(roles);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

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
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
         return userMapper.mapToResponse(user);
    }

    @Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.mapToResponse(user);
    }

    @Override
    public UserResponse update(String id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setId(id);
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        user = userRepository.save(user);
        return userMapper.mapToResponse(user);
    }

    @Override
    public void delete(String id) {
        userRepository.deleteById(id);
    }
}
