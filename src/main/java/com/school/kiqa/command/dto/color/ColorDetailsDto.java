package com.school.kiqa.command.dto.color;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ColorDetailsDto {

    private Long id;
    private String hexValue;
    private String colourName;
}
