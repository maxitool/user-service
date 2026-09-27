package org.example.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Operation {
    @JsonProperty("create")
    CREATE,
    @JsonProperty("delete")
    DELETE;

    public static Operation fromString(String operation) {
        for (Operation op : Operation.values()) {
            if (op.name().equals(operation)) {
                return op;
            }
        }
        throw new RuntimeException("Can't find operation " + operation);
    }
}