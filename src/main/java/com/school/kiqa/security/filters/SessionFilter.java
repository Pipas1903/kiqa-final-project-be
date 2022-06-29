package com.school.kiqa.security.filters;

import com.school.kiqa.security.UserAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class SessionFilter extends OncePerRequestFilter {
    private final String SESSION_COOKIE = "x-session";
    private final UserAuthenticationProvider authenticationProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Optional<Cookie> sessionCookie = Stream.of(Optional.ofNullable(request.getCookies())
                        .orElse(new Cookie[0]))
                .filter(cookie -> SESSION_COOKIE.equals(cookie.getName()) &&
                        Objects.nonNull(cookie.getValue()) &&
                        !cookie.getValue().isEmpty())
                .findFirst();

        try {
            sessionCookie.ifPresent(cookie -> {
                SecurityContextHolder.getContext()
                        .setAuthentication(authenticationProvider.validateSession(cookie.getValue()));
                log.info("Authenticated with session cookie (uuid)");
            });

        } catch (RuntimeException e) {
            SecurityContextHolder.clearContext();
            log.error("Session cookie validation failed");
        }

        filterChain.doFilter(request, response);
    }
}
