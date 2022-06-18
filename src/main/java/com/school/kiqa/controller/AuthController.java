package com.school.kiqa.controller;

import com.school.kiqa.command.dto.auth.CredentialsDto;
import com.school.kiqa.command.dto.auth.LoginDto;
import com.school.kiqa.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginDto> login(@Valid @RequestBody CredentialsDto credentialsDto) {
        log.info("Request received to login");
        LoginDto loggedInUser = authService.login(credentialsDto);
        log.info("User with id {} and role {} logged in",
                loggedInUser.getPrincipalDto().getId(),
                loggedInUser.getPrincipalDto().getRole());
        return ResponseEntity.ok(loggedInUser);
    }
}
