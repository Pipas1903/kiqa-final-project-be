package com.school.kiqa.util;

import com.school.kiqa.command.dto.auth.PrincipalDto;

public interface JwtService {
    String createToken(PrincipalDto principalDto);

    Long validateTokenAndGetId(String token);
}
