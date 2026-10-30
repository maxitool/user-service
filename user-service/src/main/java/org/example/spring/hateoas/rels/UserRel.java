package org.example.spring.hateoas.rels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum UserRel {
    CREATE("create"),
    UPDATE("update"),
    DELETE("delete"),
    GET_ALL_USERS("all-users"),
    FIND_USER_BY_ID("find-by-id"),
    FIND_USER_BY_EMAIL("find-by-email"),
    FIND_USER_BY_NAME("find-by-name"),
    FIND_USER_BY_AGE("find-by-age");

    private final String value;

    UserRel(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static UserRel fromString(String value) {
        return Arrays.stream(values())
                .filter(op -> op.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown user rel: '" + value + "'. Allowed values: " + Arrays.toString(values())));
    }
}
