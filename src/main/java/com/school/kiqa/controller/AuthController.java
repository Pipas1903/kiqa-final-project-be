package com.school.kiqa.controller;

import com.school.kiqa.command.dto.auth.CredentialsDto;
import com.school.kiqa.command.dto.auth.LoginDto;
import com.school.kiqa.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
    private final String COOKIE = "cookie_auth";

    @PostMapping("/login")
    public ResponseEntity<LoginDto> login(@Valid @RequestBody CredentialsDto credentials) {
        log.info("Request received to login");
        final var loggedInUser = authService.login(credentials);

        ResponseCookie cookie = ResponseCookie
                .from(COOKIE, loggedInUser.getToken())
                .httpOnly(true)
                .secure(false)
                .maxAge(24 * 60 * 60)
                .path("/")
                .build();
        log.info("Set cookie");


        log.info("User with id {} and role {} logged in",
                loggedInUser.getPrincipalDto().getId(),
                loggedInUser.getPrincipalDto().getRole());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loggedInUser);
    }
}