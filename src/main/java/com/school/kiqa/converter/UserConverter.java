package com.school.kiqa.converter;

import com.school.kiqa.command.dto.address.CreateOrUpdateAddressDto;
import com.school.kiqa.command.dto.user.CreateUserDto;
import com.school.kiqa.command.dto.user.UpdateUserDto;
import com.school.kiqa.command.dto.user.UserDetailsDto;
import com.school.kiqa.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final AddressConverter addressConverter;

    public UserDetailsDto convertEntityToUserDetailsDto(UserEntity user) {
        return UserDetailsDto.builder()
                .id(user.getId())
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
                .name(createUserDto.getName())
                .vat(createUserDto.getVat())
                .build();
    }

   /* public UserEntity convertUpdateUserDtoToUserEntity(UpdateUserDto updateUserDto) {
        return UserEntity.builder()
                .name(updateUserDto.getName())
                .dateOfBirth(updateUserDto.getDateOfBirth())
                .password(updateUserDto.getPassword())
                .vat(updateUserDto.getVat())
                .phoneNumber(updateUserDto.getPhoneNumber())
                .build();
    } */
}
