package com.buruadam.financialtracker.mapper;

import com.buruadam.financialtracker.dto.auth.RegisterRequest;
import com.buruadam.financialtracker.dto.auth.UserResponseDto;
import com.buruadam.financialtracker.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(RegisterRequest registerRequest);

    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    UserResponseDto toResponseDto(User user);
}
