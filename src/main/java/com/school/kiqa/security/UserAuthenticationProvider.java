package com.school.kiqa.security;

import com.school.kiqa.command.dto.auth.PrincipalDto;
import com.school.kiqa.enums.UserType;
import com.school.kiqa.exception.notFound.UserNotFoundException;
import com.school.kiqa.persistence.entity.UserEntity;
import com.school.kiqa.persistence.repository.UserRepository;
import com.school.kiqa.util.JwtService;
import com.school.kiqa.util.UuidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static com.school.kiqa.exception.ErrorMessageConstants.USER_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserAuthenticationProvider {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UuidService uuidService;

    public Authentication validateToken(String token) {
        Long userId = jwtService.validateTokenAndGetId(token);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error(String.format(USER_NOT_FOUND, userId));
                    return new UserNotFoundException(String.format(USER_NOT_FOUND, userId));
                });

        PrincipalDto principalDto = PrincipalDto.builder()
                .role(user.getUserType())
                .name(user.getName())
                .id(user.getId())
                .build();

        return new UsernamePasswordAuthenticationToken(
                principalDto,
                null,
                Collections.singletonList(new SimpleGrantedAuthority(principalDto.getRole().name())));
    }

    public Authentication validateSession(String uuid) {
        Long sessionId = uuidService.verifyUuid(uuid);

        PrincipalDto principalDto = PrincipalDto.builder()
                .role(UserType.USER)
                .name("unlogged user")
                .id(sessionId)
                .build();

        return new UsernamePasswordAuthenticationToken(
                principalDto,
                null,
                Collections.singletonList(new SimpleGrantedAuthority(principalDto.getRole().name())));
    }

}
