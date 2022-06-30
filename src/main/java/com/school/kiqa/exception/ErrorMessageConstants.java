package com.school.kiqa.exception;

public class ErrorMessageConstants {

    // address
    public static final String ADDRESS_NOT_FOUND = "Couldn't find address with id %s";
    public static final String NO_ADDRESS_GIVEN = "Please provide an address";
    public static final String INVALID_ADDRESS_ID = "Provided address id %s is doesn't belong to user with id %s";

    // user
    public static final String USER_DOESNT_HAVE_ADDRESS = "User doesn't have any addresses, please provide one before continuing";
    public static final String USER_ALREADY_EXISTS = "User with email %s already exists";
    public static final String USER_NOT_FOUND = "Couldn't find user with id %s";
    public static final String INVALID_VAT = "Insert a valid VAT number";
    public static final String INVALID_PHONE_NUMBER = "Insert a valid phone number";
    public static final String INVALID_NAME = "Name is too small";
    public static final String INVALID_ORDER_ID = "Provided order id %s doesn't belong to user with id %s";
    public static final String INVALID_ORDER_ID_SESSION = "Provided order id %s doesn't belong to session with id %s";

    // brand
    public static final String BRAND_ALREADY_EXISTS = "Brand with name %s already exists";
    public static final String BRAND_NOT_FOUND_BY_ID = "Couldn't find brand with id %s";
    public static final String BRAND_NOT_FOUND_BY_NAME = "Couldn't find brand with name %s";

    // category
    public static final String CATEGORY_ALREADY_EXISTS = "Category with name %s already exists";
    public static final String CATEGORY_NOT_FOUND_BY_NAME = "Couldn't find category with name %s";
    public static final String CATEGORY_NOT_FOUND_BY_ID = "Couldn't find category with id %s";

    // color
    public static final String COLOR_HEX_VALUE_ALREADY_EXISTS = "Color with hex value %s already exists";
    public static final String COLOR_NAME_ALREADY_EXISTS = "Color with name %s already exists";
    public static final String COLOR_NOT_FOUND_BY_HEX_VALUE = "Couldn't find color with hex value %s";
    public static final String COLOR_NOT_FOUND_BY_ID = "Couldn't find color with id %s";
    public static final String COLOR_NOT_FOUND = "Couldn't find color with id %s";

    // util
    public static final String INVALID_HEADER = "Given header doesn't match expected value. Session not created.";
    public static final String NO_RESULTS_FOUND = "No product matches given parameters";

    // product
    public static final String PRODUCT_NOT_FOUND = "Couldn't find product with id %s";
    public static final String PRODUCT_TYPE_NOT_FOUND_BY_NAME = "Couldn't find product type with name %s";

    // order
    public static final String ORDER_NOT_FOUND = "Couldn't find order with id %s";
    public static final String ORDER_PRODUCT_NOT_FOUND = "Couldn't find order product with id %s";

    // auth
    public static final String EQUAL_PASSWORDS = "New password can't be the same as old password";
    public static final String PASSWORDS_DONT_MATCH = "Saved password doesn't match old password";
    public static final String WRONG_CREDENTIALS = "Invalid Email and/or Password";

    // session
    public static final String SESSION_NOT_FOUND_UUID = "Couldn't find session with uuid %s";
    public static final String SESSION_NOT_FOUND = "Couldn't find session with id %s";
}
