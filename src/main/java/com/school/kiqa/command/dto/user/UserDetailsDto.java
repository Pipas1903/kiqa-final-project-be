package com.school.kiqa.command.dto.user;

import com.school.kiqa.command.dto.address.AddressDetailsDto;
import com.school.kiqa.command.dto.order.OrderDetailsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailsDto {
    private Long id;
    private String name;
    private String email;
    private LocalDate dateOfBirth;
    private Integer vat;
    private String phoneNumber;
    private List<AddressDetailsDto> addressList;
    private List<OrderDetailsDto> orderHistory;
}
