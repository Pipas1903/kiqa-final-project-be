package com.school.kiqa.command.dto.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrUpdateAddressDto {
    @NotBlank
    private String cityName;
    @NotBlank
    private String country;
    @NotBlank
    private String doorNumber;
    private String floorNumber;
    @NotBlank
    private String streetName;
    @NotBlank
    private String zipCode;
    @NotBlank
    private boolean isMain;
}
