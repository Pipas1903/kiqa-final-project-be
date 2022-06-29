package com.school.kiqa.command.dto.address;

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
public class AddressDetailsDto {
    private String city;
    private String country;
    private String doorNumber;
    private String floorNumber;
    private String street;
    private String zipCode;
    private Boolean isMain;
}
