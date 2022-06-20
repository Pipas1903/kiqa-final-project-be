package com.school.kiqa.command.dto.brand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrUpdateBrandDto {
    private String name;
    private String imageLink;
}
