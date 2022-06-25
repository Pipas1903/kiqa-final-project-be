package com.school.kiqa.controller;

import com.school.kiqa.command.dto.user.CreateUserDto;
import com.school.kiqa.command.dto.user.UpdateUserDto;
import com.school.kiqa.command.dto.user.UserDetailsDto;
import com.school.kiqa.enums.UserType;
import com.school.kiqa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    @PreAuthorize("@authorized.hasRole('ADMIN')")
    public ResponseEntity<UserDetailsDto> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        log.info("Request received to create user with role {}", UserType.USER);
        UserDetailsDto user = userService.createUser(createUserDto, UserType.USER);
        log.info("Returning created user");
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users")
    @PreAuthorize("@authorized.hasRole('ADMIN')")
    public ResponseEntity<List<UserDetailsDto>> getAllUsers() {
        log.info("Request received to get all users");
        List<UserDetailsDto> users = userService.getAllUsers();
        log.info("Returning users");
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDetailsDto> getUserById(@PathVariable Long id) {
        log.info("Request received to get user with id {}", id);
        UserDetailsDto user = userService.getUserById(id);
        log.info("Returned user with id {}", id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDetailsDto> updateUserById(@PathVariable Long id,
                                                         @RequestBody UpdateUserDto updateUserDto) {
        log.info("Request received to update user with id {}", id);
        UserDetailsDto user = userService.updateUserById(updateUserDto, id);
        log.info("Returned updated user with id {}", id);
        return ResponseEntity.ok(user);
    }

}