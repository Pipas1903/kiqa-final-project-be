package com.school.kiqa.util;

import com.school.kiqa.command.dto.auth.PrincipalDto;
import com.school.kiqa.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtServiceImplNew implements JwtService {

    private final JwtProperties jwtProperties;

    @Override
    public String createToken(PrincipalDto principalDto) {
        return Jwts.builder()
                .setSubject(principalDto.getId().toString())
                .claim("name", principalDto.getName())
                .claim("userType", principalDto.getRole())
                .setIssuer("kiqa")
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtProperties.getExpiration()))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();
    }

    @Override
    public Long validateTokenAndGetId(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(jwtProperties.getSecret())
                .requireIssuer("kiqa")
                .parseClaimsJws(token);
        Long userId = Long.parseLong(claims.getBody().getSubject());
        log.info("Decoded token for user with id {}", userId);
        return userId;
    }
}
