package com.example.user.common;

public interface ResponseMessage {

    String SUCCESS = "Succes.";

    String VALIDATION_FAIL = "Validation failed.";
    String DUPLICATE_EMAIL = "Duplicate Email.";
    String DUPLICATE_NICKNAME = "Duplicate Nickname.";

    String INVALID_NICKNAME = "Invalid Nickname.";

    String SIGN_IN_FAIL = "Login information mismatch.";
    String CERTIFICATION_FAIL = "Certification failed.";

    String MAIL_FAIL = "Mail send failed.";

    String DATABASE_ERROR = "Database error.";
}
