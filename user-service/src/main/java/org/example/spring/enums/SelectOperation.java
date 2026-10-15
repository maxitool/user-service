package org.example.spring.enums;

import java.util.Arrays;

public enum SelectOperation {
    FIND_BY_ID("findById"),
    FIND_ALL("findAll"),
    FIND_BY_EMAIL("findByEmail"),
    FIND_BY_NAME("findByName"),
    FIND_BY_AGE("findByAge");

    private final String value;

    SelectOperation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SelectOperation fromString(String value) {
        return Arrays.stream(values())
                .filter(op -> op.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown select operation: '" + value + "'. Allowed values: " + Arrays.toString(values())));
    }
}
