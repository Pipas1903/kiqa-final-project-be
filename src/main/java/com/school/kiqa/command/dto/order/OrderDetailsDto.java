package com.school.kiqa.command.dto.order;

import com.school.kiqa.command.dto.address.AddressDetailsDto;
import com.school.kiqa.command.dto.orderProduct.OrderProductDetailsDto;
import com.school.kiqa.persistence.entity.OrderProductEntity;
import com.school.kiqa.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailsDto {
    private Long id;
    private Double totalPrice;
    private Boolean status;
    private LocalDate creationDate;
    private List<OrderProductDetailsDto> orderProductDetailsDtoList;
    private Long userId;
    private AddressDetailsDto addressDetailsDto;
}
