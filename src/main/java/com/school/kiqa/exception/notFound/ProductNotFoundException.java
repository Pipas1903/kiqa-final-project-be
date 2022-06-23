package com.school.kiqa.exception.notFound;

import com.school.kiqa.exception.KiqaException;

public class ProductNotFoundException extends KiqaException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
