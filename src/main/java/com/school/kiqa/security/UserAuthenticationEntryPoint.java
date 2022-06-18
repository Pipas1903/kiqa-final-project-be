package com.school.kiqa.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.kiqa.exception.KiqaError;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        MAPPER.writeValue(
                response.getOutputStream(),
                KiqaError.builder()
                        .timestamp(new Date())
                        .httpMethod(request.getMethod())
                        .exception(authException.getClass().getSimpleName())
                        .message(authException.getMessage())
                        //TODO: figure out why getRequestURI and not getServletPath
                        .path(request.getRequestURI())
                        .build()
        );
    }
}
