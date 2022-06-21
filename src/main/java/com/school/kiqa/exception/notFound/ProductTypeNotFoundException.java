package com.school.kiqa.exception.notFound;

import com.school.kiqa.exception.KiqaException;

public class ProductTypeNotFoundException extends KiqaException {
    public ProductTypeNotFoundException(String message) {
        super(message);
    }
}
