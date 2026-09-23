package org.example.spring.exception;

public final class ErrorMessage {
    private ErrorMessage() {
    }

    public static final String MALFORMED_JSON_REQUEST = "Malformed JSON Request";

    public static final String UNKNOWN_TYPE = "Unknown type";

    public static final String VALIDATION_ERROR = "Validation error";

    public static final String METHOD_NOT_ALLOWED = "Method Not Allowed";

    public static final String MISSING_PARAMETER = "Missing Parameter";

    public static final String RESOURCE_NOT_FOUND = "Resource Not Found";

    public static final String USER_NOT_FOND_ID = "User id %s not found";

    public static final String USER_NOT_FOND_EMAIL = "User email %s not found";

    public static final String USER_ALREADY_EXISTS = "A user with this email %s already exists";

    public static final String INTERNAL_ERROR = "Something went wrong";
}
