package org.example.console.readers.primitives.responses;

public class BooleanResponse extends StringResponse {
    public boolean booleanData = false;

    public BooleanResponse(StringResponse stringResponse) {
        this.state = stringResponse.state;
        this.stringData = stringResponse.stringData;
        this.errorMessage = stringResponse.errorMessage;
    }
}
