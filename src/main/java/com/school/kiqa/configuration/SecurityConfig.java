package com.school.kiqa.configuration;

import com.school.kiqa.security.AuthorizationValidator;
import com.school.kiqa.security.UserAuthenticationEntryPoint;
import com.school.kiqa.security.UserAuthenticationProvider;
import com.school.kiqa.security.filters.CookieFilter;
import com.school.kiqa.security.filters.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserAuthenticationEntryPoint authenticationEntryPoint;
    private final UserAuthenticationProvider authenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .addFilterBefore(new JwtFilter(authenticationProvider), BasicAuthenticationFilter.class)
                .addFilterBefore(new CookieFilter(authenticationProvider), JwtFilter.class)
                .csrf()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/swagger-ui.html", "/swagger-ui/*", "/v3/api-docs", "/v3/api-docs/*")
                .permitAll()
                .antMatchers(HttpMethod.GET,
                        "/products",
                        "/products/*",
                        "/categories",
                        "/brands",
                        "/brands/*",
                        "/products/search/*",
                        "/products/search",
                        "/products/related",
                        "/products/related/*"
                )
                .permitAll()
                .antMatchers(HttpMethod.POST, "/login", "/users")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
        ;
    }


    @Bean
    public AuthorizationValidator authorized() {
        return new AuthorizationValidator();
    }


    //TODO: Research about this
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
