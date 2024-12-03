package com.example.ebankify.mapper;
import com.example.ebankify.domain.dtos.UserAuthDto;
import com.example.ebankify.domain.dtos.UserDto;
import com.example.ebankify.domain.entities.User;
import com.example.ebankify.domain.requests.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDto toDto(User user);
    UserAuthDto toUserAuthDto(User user);
    User toEntity(UserDto userDto);
    User toEntity(UserRequest userRequest);
}
