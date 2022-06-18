package com.school.kiqa.service;

import com.school.kiqa.command.dto.auth.CredentialsDto;
import com.school.kiqa.command.dto.auth.LoginDto;
import com.school.kiqa.command.dto.auth.PrincipalDto;
import com.school.kiqa.exception.WrongCredentialsException;
import com.school.kiqa.persistence.entity.UserEntity;
import com.school.kiqa.persistence.repository.UserRepository;
import com.school.kiqa.util.JwtManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.school.kiqa.exception.ErrorMessageConstants.WRONG_CREDENTIALS;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtManager jwt;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginDto login(CredentialsDto credentials) {
        UserEntity user = userRepository.findByEmail(credentials.getEmail())
                .orElseThrow(() -> {
                    log.error("Couldn't find any user with email {} in database", credentials.getEmail());
                    return new WrongCredentialsException(WRONG_CREDENTIALS);
                });

        if (!passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            log.error("Password doesn't match");
            throw new WrongCredentialsException(WRONG_CREDENTIALS);
        }

        PrincipalDto principalDto = PrincipalDto.builder()
                .id(user.getId())
                .name(user.getName())
                .role(user.getUserType())
                .build();

        String token = jwt.createToken(principalDto);
        log.info("Generated token for user with id {}", user.getId());

        return LoginDto.builder()
                .principalDto(principalDto)
                .token(token)
                .build();
    }
}
