package org.example.kafka;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum Operation {
    CREATE("create"),
    DELETE("delete");

    private final String value;

    Operation(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Operation fromString(String value) {
        return Arrays.stream(values())
                .filter(op -> op.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown operation: '" + value + "'. Allowed values: " + Arrays.toString(values())));
    }
}