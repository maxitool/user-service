package org.example.console.readers.primitives;

import org.example.console.readers.primitives.responses.LongResponse;
import org.example.console.readers.primitives.responses.StringResponse;

public class LongConsoleReader extends StringConsoleReader {

    public static synchronized LongResponse getLongData() {
        StringResponse stringResponse = getStringData();
        LongResponse longResponse = new LongResponse(stringResponse);
        if (longResponse.state != StringResponse.States.OK) {
            return longResponse;
        }
        try {
            longResponse.longData = Integer.parseInt(longResponse.stringData.replace(" ", ""));
        } catch (NumberFormatException e) {
            System.out.println("Can't convert the wrote data to long. " + e.getMessage());
            longResponse.errorMessage = e.getMessage();
            longResponse.state = StringResponse.States.CANT_CONVERT;
        }
        return longResponse;
    }
}
