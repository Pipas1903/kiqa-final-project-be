package com.school.kiqa.command.dto.auth;

import com.school.kiqa.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrincipalDto {
    private UserType role;
    private String name;
    private Long id;
}
