package com.school.kiqa.command.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserDto {

    @NotBlank(message = "Insert a name")
    private String name;

    @Email(message = "Insert a valid email.")
    private String email;

    @NotBlank(message = "Insert a valid date of birth")
    private LocalDate dateOfBirth;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "password doesn't match the requirements")
    private String password;

    @Pattern(regexp = "^\\d{9}$",
            message = "invalid vat"
    )
    private Integer vat;

    private String phoneNumber;
}
