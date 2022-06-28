package com.school.kiqa.exception;

import com.school.kiqa.exception.alreadyExists.AlreadyExistsException;
import com.school.kiqa.exception.alreadyExists.ColorAlreadyExistsException;
import com.school.kiqa.exception.alreadyExists.UserAlreadyExistsException;
import com.school.kiqa.exception.notFound.BrandNotFoundException;
import com.school.kiqa.exception.notFound.CategoryNotFoundException;
import com.school.kiqa.exception.notFound.ColorNotFoundException;
import com.school.kiqa.exception.notFound.OrderNotFoundException;
import com.school.kiqa.exception.notFound.OrderProductNotFoundException;
import com.school.kiqa.exception.notFound.ProductNotFoundException;
import com.school.kiqa.exception.notFound.ProductTypeNotFoundException;
import com.school.kiqa.exception.notFound.ResultsNotFoundException;
import com.school.kiqa.exception.notFound.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class KiqaExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(
            value = {
                    CategoryNotFoundException.class,
                    ColorNotFoundException.class,
                    ProductNotFoundException.class,
                    BrandNotFoundException.class,
                    ProductTypeNotFoundException.class,
                    UserNotFoundException.class,
                    ResultsNotFoundException.class,
                    OrderNotFoundException.class,
                    OrderProductNotFoundException.class
            }
    )
    public ResponseEntity<KiqaError> handleNotFoundException(Exception ex, HttpServletRequest req) {
        KiqaError error = KiqaError.builder()
                .message(ex.getMessage())
                .exception(ex.getClass().getSimpleName())
                .path(req.getRequestURI())
                .httpMethod(req.getMethod())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(
            value = {
                    AlreadyExistsException.class,
                    ColorAlreadyExistsException.class,
                    UserAlreadyExistsException.class
            }
            )
    public ResponseEntity<KiqaError> handleAlreadyExistsException(Exception ex, HttpServletRequest req) {
        KiqaError error = KiqaError.builder()
                .message(ex.getMessage())
                .exception(ex.getClass().getSimpleName())
                .path(req.getRequestURI())
                .httpMethod(req.getMethod())
                .build();

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(
            value = {
                    WrongCredentialsException.class
            }
            )
    public ResponseEntity<KiqaError> handleWrongCredentialsException(Exception ex, HttpServletRequest req) {
        KiqaError error = KiqaError.builder()
                .message(ex.getMessage())
                .exception(ex.getClass().getSimpleName())
                .path(req.getRequestURI())
                .httpMethod(req.getMethod())
                .build();

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<String> validationList = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ValidationError error = ValidationError.builder()
                .failedValidationsList(validationList)
                .exception(ex.getClass().getSimpleName())
                .message(ex.getMessage())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();

        log.error("Validation error list : " + validationList);
        return new ResponseEntity<>(error, status);
    }
}
