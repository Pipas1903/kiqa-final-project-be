package com.school.kiqa.service;

import com.school.kiqa.command.dto.auth.CredentialsDto;
import com.school.kiqa.command.dto.auth.LoginDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {

    LoginDto login(CredentialsDto credentials);
}
