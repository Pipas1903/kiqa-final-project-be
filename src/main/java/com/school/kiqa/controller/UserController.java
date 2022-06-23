package com.school.kiqa.controller;

import com.school.kiqa.command.dto.user.CreateUserDto;
import com.school.kiqa.command.dto.user.UserDetailsDto;
import com.school.kiqa.enums.UserType;
import com.school.kiqa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<UserDetailsDto> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        log.info("Request received to create user with role {}", UserType.USER);
        UserDetailsDto user = userService.createUser(createUserDto, UserType.USER);
        log.info("Returning created user");
        return ResponseEntity.ok(user);
    }
}
