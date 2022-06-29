package com.school.kiqa.command.dto.user;

import com.school.kiqa.command.dto.address.AddressDetailsDto;
import com.school.kiqa.command.dto.address.CreateOrUpdateAddressDto;
import com.school.kiqa.persistence.entity.AddressEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserDto {

    private String name;

    private LocalDate dateOfBirth;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "password doesn't match the requirements")
    private String password;

    private Integer vat;

    private String phoneNumber;

    private List<CreateOrUpdateAddressDto> addressList;
}
