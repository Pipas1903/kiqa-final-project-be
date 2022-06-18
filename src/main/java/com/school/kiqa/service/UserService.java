package com.school.kiqa.service;

import com.school.kiqa.command.Paginated;
import com.school.kiqa.command.dto.user.CreateUserDto;
import com.school.kiqa.command.dto.user.UpdateUserDto;
import com.school.kiqa.command.dto.user.UserDetailsDto;
import com.school.kiqa.enums.UserType;
import org.springframework.data.domain.PageRequest;

public interface UserService {

    UserDetailsDto createUser(CreateUserDto dto, UserType userType);

    UserDetailsDto getUserById(Long id);

    Paginated<UserDetailsDto> getAllUsers(PageRequest page);

    UserDetailsDto updateUser(UpdateUserDto updateUserDto);
}
