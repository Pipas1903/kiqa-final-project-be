package com.school.kiqa.security;

import com.school.kiqa.command.dto.auth.PrincipalDto;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthorizationValidator {

    public boolean hasRole(String userRole) {
        return userRole.equals(getPrincipal().getRole().toString());
    }

    public boolean isUser(Long userId) {
        return userId.equals(getPrincipal().getId());
    }

    private PrincipalDto getPrincipal() {
        return (PrincipalDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
