package com.school.kiqa.security.filters;

import com.school.kiqa.security.UserAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
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
public class CookieFilter extends OncePerRequestFilter {

    private final String COOKIE = "cookie_auth";
    private final UserAuthenticationProvider authenticationProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Optional<Cookie> authCookie = Stream.of(Optional.ofNullable(request.getCookies())
                        .orElse(new Cookie[0]))
                .filter(cookie -> COOKIE.equals(cookie.getName()) &&
                        Objects.nonNull(cookie.getValue()) &&
                        !cookie.getValue().isEmpty())
                .findFirst();

        try {
            authCookie.ifPresent(cookie -> {
                final var authentication = authenticationProvider.validateToken(cookie.getValue());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Authenticated with auth cookie");
                Optional<Cookie> sessionCookie = Stream.of(Optional.ofNullable(request.getCookies())
                                .orElse(new Cookie[0]))
                        .filter(otherCookie -> "x-session".equals(otherCookie.getName()) &&
                                Objects.nonNull(otherCookie.getValue()) &&
                                !otherCookie.getValue().isEmpty())
                        .findFirst();
                Cookie invalidateCookie = new Cookie("x-session", null);
                invalidateCookie.setMaxAge(0);
                sessionCookie.ifPresent(cookie1 -> response.addCookie(invalidateCookie));

                log.info("invalidated session cookie");
            });

        } catch (RuntimeException e) {
            SecurityContextHolder.clearContext();
            log.error("Cookie validation failed");
        }

        filterChain.doFilter(request, response);
    }
}
