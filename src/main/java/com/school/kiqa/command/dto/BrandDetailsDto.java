package com.school.kiqa.command.dto;

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
public class BrandDetailsDto {
    private String name;
    private String imageLink;
    private Long id;
}
