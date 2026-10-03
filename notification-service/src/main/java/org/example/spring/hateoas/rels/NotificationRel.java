package org.example.spring.hateoas.rels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum NotificationRel {
    SEND_USER_CREATED("send-user-created"),
    SEND_USER_DELETED("send-user-deleted");

    private final String value;

    NotificationRel(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static NotificationRel fromString(String value) {
        return Arrays.stream(values())
                .filter(op -> op.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown notification rel: '" + value +
                                "'. Allowed values: " + Arrays.toString(values())));
    }
}
