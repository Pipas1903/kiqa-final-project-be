package com.school.kiqa.exception.notFound;

import com.school.kiqa.exception.KiqaException;

public class OrderProductNotFoundException extends KiqaException {
    public OrderProductNotFoundException(String message) {
        super(message);
    }
}
