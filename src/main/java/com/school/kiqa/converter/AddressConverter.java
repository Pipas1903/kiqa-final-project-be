package com.school.kiqa.converter;

import com.school.kiqa.command.dto.address.AddressDetailsDto;
import com.school.kiqa.command.dto.address.CreateOrUpdateAddressDto;
import com.school.kiqa.persistence.entity.AddressEntity;
import org.springframework.stereotype.Component;

@Component
public class AddressConverter {
    public AddressEntity convertCreateDtoToAddressEntity(CreateOrUpdateAddressDto addressDto){
        return AddressEntity.builder()
                .cityName(addressDto.getCityName())
                .country(addressDto.getCountry())
                .doorNumber(addressDto.getDoorNumber())
                .floorNumber(addressDto.getFloorNumber())
                .streetName(addressDto.getStreetName())
                .zipCode(addressDto.getZipCode())
                .build();
    }
    public AddressDetailsDto convertEntityToAddressDetailsDto(AddressEntity address){
        return AddressDetailsDto.builder()
                .id(address.getId())
                .cityName(address.getCityName())
                .country(address.getCountry())
                .doorNumber(address.getDoorNumber())
                .floorNumber(address.getFloorNumber())
                .streetName(address.getStreetName())
                .zipCode(address.getZipCode())
                .isMain(address.getIsMain())
                .build();
    }
}
