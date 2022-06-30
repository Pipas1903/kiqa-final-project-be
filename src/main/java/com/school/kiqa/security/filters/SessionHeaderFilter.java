package com.school.kiqa.security.filters;

import com.school.kiqa.security.UserAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class SessionHeaderFilter extends OncePerRequestFilter {

    private final UserAuthenticationProvider authenticationProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (Objects.nonNull(header)) {
            String[] sessionElements = header.split(" ");
            if (sessionElements.length == 2 && "Uuid".equals(sessionElements[0])) {
                try {
                    Authentication authentication = authenticationProvider.validateSession(sessionElements[1]);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("authenticated with session token");
                } catch (RuntimeException e) {
                    SecurityContextHolder.clearContext();
                    log.error("uuid verification failed");
                    throw e;
                }
            }

        }

        filterChain.doFilter(request, response);
    }
}
