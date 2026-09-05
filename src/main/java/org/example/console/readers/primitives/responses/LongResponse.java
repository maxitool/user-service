package org.example.console.readers.primitives.responses;

public class LongResponse extends StringResponse {
    public long longData = 0;

    public LongResponse(StringResponse stringResponse) {
        this.state = stringResponse.state;
        this.stringData = stringResponse.stringData;
        this.errorMessage = stringResponse.errorMessage;
    }
}
