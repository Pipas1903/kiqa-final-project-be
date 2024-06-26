package com.school.kiqa.service;

import com.school.kiqa.command.Paginated;
import com.school.kiqa.command.dto.address.CreateOrUpdateAddressDto;
import com.school.kiqa.command.dto.user.ChangePasswordDto;
import com.school.kiqa.command.dto.user.CreateUserDto;
import com.school.kiqa.command.dto.user.UpdateUserDto;
import com.school.kiqa.command.dto.user.UserDetailsDto;
import com.school.kiqa.enums.UserType;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface UserService {

    UserDetailsDto createUser(CreateUserDto dto, UserType userType);

    UserDetailsDto getUserById(Long id);

    List<UserDetailsDto> getAllUsers();

    UserDetailsDto updateUserById(UpdateUserDto updateUserDto, Long userId);

    UserDetailsDto addAddress(CreateOrUpdateAddressDto addressDto, Long userId);

    UserDetailsDto removeAddress(Long addressId, Long userId);

    UserDetailsDto updatePassword(ChangePasswordDto changePassword, Long userId);
}
