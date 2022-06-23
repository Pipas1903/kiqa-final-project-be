package com.school.kiqa.service;

import com.school.kiqa.command.dto.user.CreateUserDto;
import com.school.kiqa.command.dto.user.UpdateUserDto;
import com.school.kiqa.command.dto.user.UserDetailsDto;
import com.school.kiqa.converter.AddressConverter;
import com.school.kiqa.converter.UserConverter;
import com.school.kiqa.enums.UserType;
import com.school.kiqa.exception.alreadyExists.UserAlreadyExistsException;
import com.school.kiqa.exception.notFound.ProductNotFoundException;
import com.school.kiqa.exception.notFound.UserNotFoundException;
import com.school.kiqa.persistence.entity.AddressEntity;
import com.school.kiqa.persistence.entity.ProductEntity;
import com.school.kiqa.persistence.entity.UserEntity;
import com.school.kiqa.persistence.repository.AddressRepository;
import com.school.kiqa.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.school.kiqa.exception.ErrorMessageConstants.*;

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
            log.info("setting user phone number");
            user.setPhoneNumber(dto.getPhoneNumber());
        }

        if (dto.getMainAddress() == null) {
            log.warn("User did not provide main address, saving with no address");
            final var savedUser = userRepository.save(user);
            log.info("Saved new user with id {} to database", savedUser.getId());
            return userConverter.convertEntityToUserDetailsDto(savedUser);
        }

        AddressEntity address = addressConverter.convertCreateDtoToAddressEntity(dto.getMainAddress());
        address.setIsMain(true);
        addressRepository.save(address);
        log.info("Saved user main address to database");

        List<AddressEntity> addressEntities = new ArrayList<>();
        addressEntities.add(address);

        user.setAddressEntities(addressEntities);
        log.info("Set user main address");

        final var savedUser = userRepository.save(user);
        log.info("Saved new user with id {} to database", savedUser.getId());
        return userConverter.convertEntityToUserDetailsDto(savedUser);
    }

    @Override
    public UserDetailsDto getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("user with id {} does not exist", id);
                    return new UserNotFoundException(String.format(USER_NOT_FOUND, id));
                });


        log.info("returned user with id {} successfully", id);
        return userConverter.convertEntityToUserDetailsDto(userEntity);
    }


    @Override
    public List<UserDetailsDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userConverter::convertEntityToUserDetailsDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDetailsDto updateUserById(UpdateUserDto updateUserDto, Long id) {

        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("user with id {} does not exist", id);
                    return new UserNotFoundException(String.format(USER_NOT_FOUND, id));
                });


        if (updateUserDto.getName() != null) {
            userEntity.setName(updateUserDto.getName());
        }

        if (updateUserDto.getDateOfBirth() != null) {
            userEntity.setDateOfBirth(updateUserDto.getDateOfBirth());
        }

        if (updateUserDto.getPassword()!= null) {
            userEntity.setPassword(passwordEncoder.encode(updateUserDto.getPassword()));
            log.info("Successfully encrypted user password");


        }

        if (updateUserDto.getVat() != null) {
            userEntity.setVat(updateUserDto.getVat());

        }

        if (updateUserDto.getPhoneNumber() != null) {
            userEntity.setPhoneNumber(updateUserDto.getPhoneNumber());
        }

        if (updateUserDto.getAddressList() != null ) {
           List<AddressEntity> addressEntityList =  updateUserDto.getAddressList().stream().map(addressConverter::convertCreateDtoToAddressEntity)
                    .collect(Collectors.toList());

           userEntity.setAddressEntities(addressEntityList);

        }

        final var savedUser = userRepository.save(userEntity);
        log.info("Saved updated user with id {} to database", savedUser.getId());

        log.info("user with id {} was successfully updated", id);
        return userConverter.convertEntityToUserDetailsDto(userEntity);
    }

}