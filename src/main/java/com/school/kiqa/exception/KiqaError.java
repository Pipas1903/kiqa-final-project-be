package com.school.kiqa.exception;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class KiqaError {
    private String message;
    private String exception;
    private String path;
    private String httpMethod;
    private Date timestamp;
}
