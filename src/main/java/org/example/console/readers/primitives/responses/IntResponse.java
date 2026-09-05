package org.example.console.readers.primitives.responses;

public class IntResponse extends StringResponse {
    public int intData = 0;

    public IntResponse(StringResponse stringResponse) {
        this.state = stringResponse.state;
        this.stringData = stringResponse.stringData;
        this.errorMessage = stringResponse.errorMessage;
    }
}
