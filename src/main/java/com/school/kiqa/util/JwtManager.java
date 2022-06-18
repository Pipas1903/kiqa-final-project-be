package com.school.kiqa.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.impl.JWTParser;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.school.kiqa.command.dto.auth.PrincipalDto;
import com.school.kiqa.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtManager {

    private final JwtProperties properties;

    public String createToken(PrincipalDto principal) {
        return JWT.create()
                .withClaim("name", principal.getName())
                .withClaim("id", principal.getId())
                .withClaim("userType", principal.getRole().toString())
                .withIssuedAt(Date.from(Instant.now()))
                .withIssuer("kiqa")
                .withExpiresAt(new Date(new Date().getTime() + properties.getExpiration()))
                .sign(Algorithm.HMAC256(properties.getSecret()));
    }

    public Long validateTokenAndGetId(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(properties.getSecret()))
                .withIssuer("kiqa")
                .withClaimPresence("name")
                .withClaimPresence("id")
                .withClaimPresence("userType")
                .build();

        DecodedJWT decodedJWT = verifier.verify(token);
        System.out.println("Verify JWT token success.");

        Long userId = decodedJWT.getClaim("id").asLong();
        log.info("Decoded token for user with id {}", userId);
        return userId;
    }
}
