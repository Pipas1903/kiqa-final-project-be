package com.school.kiqa.service;

import com.school.kiqa.command.Paginated;
import com.school.kiqa.command.dto.user.CreateUserDto;
import com.school.kiqa.command.dto.user.UpdateUserDto;
import com.school.kiqa.command.dto.user.UserDetailsDto;
import com.school.kiqa.converter.AddressConverter;
import com.school.kiqa.converter.UserConverter;
import com.school.kiqa.enums.UserType;
import com.school.kiqa.exception.UserAlreadyExistsException;
import com.school.kiqa.persistence.entity.AddressEntity;
import com.school.kiqa.persistence.entity.UserEntity;
import com.school.kiqa.persistence.repository.AddressRepository;
import com.school.kiqa.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.school.kiqa.exception.ErrorMessageConstants.USER_ALREADY_EXISTS;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserConverter userConverter;
    private final AddressConverter addressConverter;

    @Override
    public UserDetailsDto createUser(CreateUserDto dto, UserType userType) {
        userRepository.findByEmail(dto.getEmail())
                .ifPresent(user -> {
                    log.warn(String.format(USER_ALREADY_EXISTS, dto.getEmail()));
                    throw new UserAlreadyExistsException(String.format(USER_ALREADY_EXISTS, dto.getEmail()));
                });

        UserEntity user = userConverter.convertCreateDtoToUserEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        log.info("Successfully encrypted user password");
        user.setUserType(userType);

        if (dto.getPhoneNumber() != null) {
            user.setPhoneNumber(dto.getPhoneNumber());
            log.info("Saved user phone number");
        }

        final var savedUser = userRepository.save(user);
        log.info("Saved new user with id {} to database", savedUser.getId());

        return userConverter.convertEntityToUserDetailsDto(savedUser);
    }

    @Override
    public UserDetailsDto getUserById(Long id) {
        return null;
    }

    @Override
    public Paginated<UserDetailsDto> getAllUsers(PageRequest page) {
        return null;
    }

    @Override
    public UserDetailsDto updateUser(UpdateUserDto updateUserDto) {
        return null;
    }
}
