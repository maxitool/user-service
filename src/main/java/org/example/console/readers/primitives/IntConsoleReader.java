package org.example.console.readers.primitives;

import org.example.console.readers.primitives.responses.IntResponse;
import org.example.console.readers.primitives.responses.StringResponse;

public class IntConsoleReader extends StringConsoleReader {

    public static synchronized IntResponse getIntData() {
        StringResponse stringResponse = getStringData();
        IntResponse intResponse = new IntResponse(stringResponse);
        if (intResponse.state != StringResponse.States.OK) {
            return intResponse;
        }
        try {
            intResponse.intData = Integer.parseInt(intResponse.stringData.replace(" ", ""));
        } catch (NumberFormatException e) {
            System.out.println("Can't convert the wrote data to int. " + e.getMessage());
            intResponse.errorMessage = e.getMessage();
            intResponse.state = StringResponse.States.CANT_CONVERT;
        }
        return intResponse;
    }
}
