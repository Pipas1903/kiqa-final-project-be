package com.school.kiqa.exception.notFound;

import com.school.kiqa.exception.KiqaException;

public class OrderNotFoundException extends KiqaException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
