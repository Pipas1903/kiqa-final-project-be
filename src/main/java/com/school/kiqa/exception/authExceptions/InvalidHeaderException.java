package com.school.kiqa.exception.authExceptions;

import com.school.kiqa.exception.KiqaException;

public class InvalidHeaderException extends KiqaException {
    public InvalidHeaderException(String message) {
        super(message);
    }
}
