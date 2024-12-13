package com.example.ebankify.mapper;
import com.example.ebankify.domain.dtos.UserAuthDto;
import com.example.ebankify.domain.dtos.UserDto;
import com.example.ebankify.domain.entities.Role;
import com.example.ebankify.domain.entities.User;
import com.example.ebankify.domain.requests.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    UserAuthDto toUserAuthDto(User user);
    User toEntity(UserDto userDto);
    User toEntity(UserRequest userRequest);

    default Set<Role> map(Set<Long> roleIds) {
        if (roleIds == null) {
            return null;
        }
        return roleIds.stream()
                .map(this::mapRoleIdToRole)
                .collect(Collectors.toSet());
    }

    default Role mapRoleIdToRole(Long roleId) {
        Role role = new Role();
        role.setId(Math.toIntExact(roleId));
        return role;
    }
}
