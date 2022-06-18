package com.school.kiqa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice
public class KiqaExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(
            value = {
                    CategoryNotFoundException.class,
                    ColorNotFoundException.class,
                    ProductNotFoundException.class,
                    BrandNotFoundException.class,
                    ProductTypeNotFoundException.class
            }
    )
    public ResponseEntity<KiqaError> handleNotFoundException(Exception ex, HttpServletRequest req) {
        KiqaError error = KiqaError.builder()
                .message(ex.getMessage())
                .exception(ex.getClass().getSimpleName())
                .path(req.getRequestURI())
                .httpMethod(req.getMethod())
                .timestamp(new Date())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = AlreadyExistsException.class)
    public ResponseEntity<KiqaError> handleAlreadyExistsException(Exception ex, HttpServletRequest req) {
        KiqaError error = KiqaError.builder()
                .message(ex.getMessage())
                .exception(ex.getClass().getSimpleName())
                .path(req.getRequestURI())
                .httpMethod(req.getMethod())
                .timestamp(new Date())
                .build();

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}
