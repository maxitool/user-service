package org.example.console.readers.primitives.responses;

public class StringResponse {
    public enum States {
        NONE, OK, BACK_COMMAND, CANT_CONVERT, BAD_RESPONSE
    }

    public States state = States.NONE;
    public String stringData = "";
    public String errorMessage = "";
}
