package com.school.kiqa.command.dto.user;

import com.school.kiqa.command.dto.address.AddressDetailsDto;
import com.school.kiqa.command.dto.address.CreateOrUpdateAddressDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserDto {

    private String name;

    @Email
    private String email;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    private String password;

    @NotNull
    private Integer vat;

    private String phoneNumber;

    private CreateOrUpdateAddressDto mainAddress;

}
