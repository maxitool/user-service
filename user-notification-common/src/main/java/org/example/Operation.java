package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Operation {
    @JsonProperty("create")
    CREATE,
    @JsonProperty("delete")
    DELETE;
}