package com.example.identity.mapper;

import com.example.identity.dto.request.UserRequest;
import com.example.identity.dto.response.UserResponse;
import com.example.identity.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User mapToEntity(UserRequest request);

    UserResponse mapToResponse(User user);

    void updateUser(@MappingTarget User user, UserRequest request);

}
