package com.school.kiqa.converter;

import com.school.kiqa.command.dto.user.CreateUserDto;
import com.school.kiqa.command.dto.user.UserDetailsDto;
import com.school.kiqa.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    public UserDetailsDto convertEntityToUserDetailsDto(UserEntity user) {
        return UserDetailsDto.builder()
                .dateOfBirth(user.getDateOfBirth())
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .vat(user.getVat())
                .build();
    }

    public UserEntity convertCreateDtoToUserEntity(CreateUserDto createUserDto) {
        return UserEntity.builder()
                .email(createUserDto.getEmail())
                .password(createUserDto.getPassword())
                .dateOfBirth(createUserDto.getDateOfBirth())
                .phoneNumber(createUserDto.getPhoneNumber())
                .name(createUserDto.getName())
                .vat(createUserDto.getVat())
                .build();
    }
}
