package com.school.kiqa.service;

import com.school.kiqa.command.dto.address.CreateOrUpdateAddressDto;
import com.school.kiqa.command.dto.user.ChangePasswordDto;
import com.school.kiqa.command.dto.user.CreateUserDto;
import com.school.kiqa.command.dto.user.UpdateUserDto;
import com.school.kiqa.command.dto.user.UserDetailsDto;
import com.school.kiqa.converter.AddressConverter;
import com.school.kiqa.converter.OrderConverter;
import com.school.kiqa.converter.UserConverter;
import com.school.kiqa.enums.UserType;
import com.school.kiqa.exception.alreadyExists.PasswordMismatchException;
import com.school.kiqa.exception.alreadyExists.UserAlreadyExistsException;
import com.school.kiqa.exception.notFound.UserNotFoundException;
import com.school.kiqa.persistence.entity.AddressEntity;
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

import static com.school.kiqa.exception.ErrorMessageConstants.EQUAL_PASSWORDS;
import static com.school.kiqa.exception.ErrorMessageConstants.PASSWORDS_DONT_MATCH;
import static com.school.kiqa.exception.ErrorMessageConstants.USER_ALREADY_EXISTS;
import static com.school.kiqa.exception.ErrorMessageConstants.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserConverter userConverter;
    private final AddressConverter addressConverter;
    private final OrderConverter orderConverter;

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

        final var savedUser = userRepository.save(user);
        log.info("Saved new user with id {} to database", savedUser.getId());

        return userConverter.convertEntityToUserDetailsDto(savedUser);
    }

    @Override
    public UserDetailsDto getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("user with id {} does not exist", id);
                    throw new UserNotFoundException(String.format(USER_NOT_FOUND, id));
                });

        log.info("returned user with id {} successfully", id);
        return addConvertedLists(userEntity);
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
                    throw new UserNotFoundException(String.format(USER_NOT_FOUND, id));
                });


        if (updateUserDto.getName() != null) {
            log.info("request received to update the name of the user with id {}", id);
            userEntity.setName(updateUserDto.getName());
            log.info("name of the user with id {} was successfully updated", id);
        }

        if (updateUserDto.getDateOfBirth() != null) {
            log.info("request received to update the date of birth of the user with id {}", id);
            userEntity.setDateOfBirth(updateUserDto.getDateOfBirth());
            log.info("date of birth of the user with id {} was successfully updated", id);
        }

        if (updateUserDto.getVat() != null) {
            log.info("request received to update the vat of the user with id {}", id);
            userEntity.setVat(updateUserDto.getVat());
            log.info("vat of the user with id {} was successfully updated", id);
        }

        if (updateUserDto.getPhoneNumber() != null) {
            log.info("request received to update the phone number of the user with id {}", id);
            userEntity.setPhoneNumber(updateUserDto.getPhoneNumber());
            log.info("phone number of the user with id {} was successfully updated", id);
        }

        final var savedUser = userRepository.save(userEntity);

        if (updateUserDto.getAddressList() != null) {
            log.info("request received to update the address list of the user with id {}", id);
            List<AddressEntity> addressEntityList = updateUserDto.getAddressList().stream()
                    .map(addressConverter::convertCreateDtoToAddressEntity)
                    .collect(Collectors.toList());

            addressEntityList.forEach(address -> address.setUserEntity(savedUser));

            final var savedAddressEntityList = addressRepository.saveAll(addressEntityList);
            userEntity.setAddressEntities(savedAddressEntityList);
            log.info("address list of the user with id {} was successfully updated", id);
        }

        UserDetailsDto convertedUser = addConvertedLists(savedUser);
        log.info("user with id {} was successfully updated", id);
        return convertedUser;

    }

    @Override
    public UserDetailsDto addAddress(CreateOrUpdateAddressDto addressDto, Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("user with id {} does not exist", userId);
                    throw new UserNotFoundException(String.format(USER_NOT_FOUND, userId));
                });

        final var savedUser = userRepository.save(userEntity);
        log.info("Saved user with id {} with new address {} to database", savedUser.getId(), addressDto);

        AddressEntity address = addressConverter.convertCreateDtoToAddressEntity(addressDto);
        address.setUserEntity(userEntity);
        addressRepository.save(address);
        log.info("Saved new address to database");

        return addConvertedLists(userEntity);
    }

    @Override
    public UserDetailsDto removeAddress(Long addressId, Long userId) {
        return null;
    }

    @Override
    public UserDetailsDto updatePassword(ChangePasswordDto changePassword, Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("user with id {} does not exist", userId);
                    throw new UserNotFoundException(String.format(USER_NOT_FOUND, userId));
                });
        if (!passwordEncoder.matches(changePassword.getOldPassword(), userEntity.getPassword())) {
            log.error(PASSWORDS_DONT_MATCH);
            throw new PasswordMismatchException(PASSWORDS_DONT_MATCH);
        }
        if (changePassword.getOldPassword().equals(changePassword.getNewPassword())) {
            log.error(EQUAL_PASSWORDS);
            throw new PasswordMismatchException(EQUAL_PASSWORDS);
        }

        String newPassword = passwordEncoder.encode(changePassword.getNewPassword());
        log.info("encoded new password");
        userEntity.setPassword(newPassword);
        final var savedUser = userRepository.save(userEntity);
        log.info("saved changes to database");
        return addConvertedLists(savedUser);
    }

    private UserDetailsDto addConvertedLists(UserEntity savedUser) {
        final var convertedUser = userConverter.convertEntityToUserDetailsDto(savedUser);

        if (!savedUser.getAddressEntities().isEmpty())
            convertedUser.setAddressList(savedUser.getAddressEntities().stream()
                    .map(addressConverter::convertEntityToAddressDetailsDto)
                    .collect(Collectors.toList()));

        if (!savedUser.getOrderEntityList().isEmpty())
            convertedUser.setOrderHistory(savedUser.getOrderEntityList().stream()
                    .map(orderConverter::convertEntityToOrderDetailsDto)
                    .collect(Collectors.toList()));
        return convertedUser;
    }

}
