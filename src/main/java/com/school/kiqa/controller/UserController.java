package com.school.kiqa.controller;

import com.school.kiqa.command.dto.address.CreateOrUpdateAddressDto;
import com.school.kiqa.command.dto.user.ChangePasswordDto;
import com.school.kiqa.command.dto.user.CreateUserDto;
import com.school.kiqa.command.dto.user.UpdateUserDto;
import com.school.kiqa.command.dto.user.UserDetailsDto;
import com.school.kiqa.enums.UserType;
import com.school.kiqa.persistence.entity.EmailMessage;
import com.school.kiqa.service.EmailService;
import com.school.kiqa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/users")
    public ResponseEntity<UserDetailsDto> createUser(@RequestBody @Valid CreateUserDto createUserDto) {
        log.info("Request received to create user with role {}", UserType.USER);
        final var user = userService.createUser(createUserDto, UserType.USER);
        log.info("Returning created user");
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users")
    @PreAuthorize("@authorized.hasRole('ADMIN')")
    public ResponseEntity<List<UserDetailsDto>> getAllUsers() {
        log.info("Request received to get all users");
        final var users = userService.getAllUsers();
        log.info("Returning users");
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("@authorized.isUser(#id)")
    public ResponseEntity<UserDetailsDto> getUserById(@PathVariable Long id) {
        log.info("Request received to get user with id {}", id);
        final var user = userService.getUserById(id);
        log.info("Returned user with id {}", id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("@authorized.isUser(#id)")
    public ResponseEntity<UserDetailsDto> updateUserById(
            @PathVariable Long id,
            @RequestBody UpdateUserDto updateUserDto) {
        log.info("Request received to update user with id {}", id);
        final var user = userService.updateUserById(updateUserDto, id);
        log.info("Returned updated user with id {}", id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users/{userId}/address")
    @PreAuthorize("@authorized.isUser(#userId)")
    public ResponseEntity<UserDetailsDto> addAddress(
            @RequestBody CreateOrUpdateAddressDto addressDto,
            @PathVariable Long userId
    ) {
        log.info("Request received to add address {} to the user with id {}", addressDto, userId);
        final var user = userService.addAddress(addressDto, userId);
        log.info("Returning user with an added address");
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/users/{userId}/password")
    @PreAuthorize("@authorized.isUser(#userId)")
    public ResponseEntity<UserDetailsDto> updatePassword(
            @RequestBody ChangePasswordDto changePasswordDto,
            @PathVariable Long userId) {
        log.info("Request received to change password of user with id {}", userId);
        final var user = userService.updatePassword(changePasswordDto, userId);
        log.info("Returning user");
        return ResponseEntity.ok(user);
    }


    @PostMapping("/users/email")
    public void sendEmail(@RequestBody EmailMessage emailMessage) {

        try {
            emailService.send(emailMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
