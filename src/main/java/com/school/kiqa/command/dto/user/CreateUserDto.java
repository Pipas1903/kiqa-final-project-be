package com.school.kiqa.command.dto.user;

import com.school.kiqa.command.dto.address.CreateOrUpdateAddressDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserDto {

    @NotNull
    private String name;

    @Email(message = "Insert a valid email.")
    private String email;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "password doesn't match the requirements")
    private String password;

    @NotNull
    private Integer vat;

    private String phoneNumber;

    private CreateOrUpdateAddressDto mainAddress;

}