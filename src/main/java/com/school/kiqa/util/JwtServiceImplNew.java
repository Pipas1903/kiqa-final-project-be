package com.school.kiqa.util;

import com.school.kiqa.command.dto.auth.PrincipalDto;
import com.school.kiqa.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtServiceImplNew implements JwtService {

    private final JwtProperties jwtProperties;

    @Override
    public String createToken(PrincipalDto principalDto) {
        return null;
    }

    @Override
    public Long validateTokenAndGetId(String token) {
        return null;
    }
}
