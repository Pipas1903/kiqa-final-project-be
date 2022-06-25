package com.school.kiqa.controller;

import com.school.kiqa.command.dto.user.CreateUserDto;
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


}