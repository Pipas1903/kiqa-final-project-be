package com.school.kiqa.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@Setter
public class ValidationError {
    private List<String> failedValidationsList;
    private String exception;
    private String path;
}
