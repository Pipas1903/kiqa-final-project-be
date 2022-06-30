package com.school.kiqa.controller;

import com.school.kiqa.command.dto.auth.CredentialsDto;
import com.school.kiqa.command.dto.auth.LoginDto;
import com.school.kiqa.command.dto.auth.PrincipalDto;
import com.school.kiqa.command.dto.auth.SessionDetailsDto;
import com.school.kiqa.service.AuthService;
import com.school.kiqa.util.UuidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UuidService uuidService;
    private final String COOKIE = "cookie_auth";
    private final String SESSION_COOKIE = "x-session";

    @PostMapping("/session")
    public ResponseEntity<SessionDetailsDto> createSession(@RequestHeader(value = "identifier") String identifier) {
        log.info("Received request to create session");
        final var session = uuidService.createSession(uuidService.generateUuid(identifier));

        ResponseCookie cookie = ResponseCookie
                .from(SESSION_COOKIE, session.getTokenUuid())
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .maxAge(24 * 60 * 60)
                .path("/")
                .build();
        log.info("Set session cookie");

        log.info("User without login has now a session");


        final SessionDetailsDto sessionDetails = SessionDetailsDto.builder()
                .uuid(session.getTokenUuid())
                .id(session.getId())
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(sessionDetails);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDto> login(@Valid @RequestBody CredentialsDto credentials) {
        log.info("Request received to login");
        final var loggedInUser = authService.login(credentials);

        ResponseCookie cookie = ResponseCookie
                .from(COOKIE, loggedInUser.getToken())
                .path("/")
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .maxAge(24 * 60 * 60)
                .build();
        log.info("Set cookie");


        log.info("User with id {} and role {} logged in",
                loggedInUser.getPrincipalDto().getId(),
                loggedInUser.getPrincipalDto().getRole());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loggedInUser);
    }

    @DeleteMapping("/basic-logout")
    public ResponseEntity<Void> logout() {
        log.info("Received request to logout user");

        ResponseCookie invalidateCookie = ResponseCookie
                .from(COOKIE, null)
                .httpOnly(true)
                .secure(false)
                .maxAge(0L)
                .path("/")
                .build();

        log.info("Cookie deleted");

        SecurityContextHolder.clearContext();

        log.info("User logged out");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer ")
                .header(HttpHeaders.SET_COOKIE, invalidateCookie.toString())
                .build();
    }

    @GetMapping("/validate-token/{id}")
    @PreAuthorize("@authorized.isUser(#id)")
    public ResponseEntity<PrincipalDto> validateToken(@PathVariable Long id) {
        log.info("Received request to validate user token");
        PrincipalDto principalDto = (PrincipalDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Returning principal dto");
        return ResponseEntity.ok(principalDto);
    }
}
