package com.school.kiqa.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class KiqaError {
    private String message;
    private String exception;
    private String path;
}
