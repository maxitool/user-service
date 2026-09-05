package org.example.console.readers.primitives;

import org.example.console.readers.primitives.responses.BooleanResponse;
import org.example.console.readers.primitives.responses.StringResponse;

public class BooleanConsoleReader extends StringConsoleReader {

    public static synchronized BooleanResponse getBooleanData(boolean doConsoleLogAfterParse) {
        StringResponse stringResponse = getStringData();
        BooleanResponse booleanResponse = new BooleanResponse(stringResponse);
        booleanResponse.booleanData = false;
        if (booleanResponse.state != StringResponse.States.OK) {
            return booleanResponse;
        }
        if (booleanResponse.stringData.isEmpty()) {
            String message = "Can't convert empty data to boolean";
            System.out.println(message);
            booleanResponse.errorMessage = message;
            booleanResponse.state = StringResponse.States.CANT_CONVERT;
            return booleanResponse;
        }
        String data = booleanResponse.stringData.replace(" ", "").trim();
        data = data.toLowerCase();
        if (data.equals("true")) {
            booleanResponse.booleanData = true;
            return booleanResponse;
        }
        if (data.equals("false")) {
            return booleanResponse;
        }
        if (doConsoleLogAfterParse) {
            System.out.println("Can't convert the wrote data to boolean, data: " + booleanResponse.stringData);
        }
        booleanResponse.state = StringResponse.States.CANT_CONVERT;
        return booleanResponse;
    }

    public static synchronized BooleanResponse getBooleanData() {
        return getBooleanData(true);
    }

    public static synchronized BooleanResponse getBooleanData(String trueValue, String falseValue) {
        if (trueValue == null || falseValue == null) {
            System.out.println("trueValue = null or falseValue = null in BooleanConsoleReader.getBooleanData.");
            return new BooleanResponse(new StringResponse());
        }
        BooleanResponse booleanResponse = getBooleanData(false);
        if ((booleanResponse.state != StringResponse.States.OK && booleanResponse.state != StringResponse.States.CANT_CONVERT)
                || booleanResponse.stringData.isEmpty()) {
            return booleanResponse;
        }
        trueValue = trueValue.trim();
        trueValue = Character.toLowerCase(trueValue.charAt(0)) + trueValue.substring(1);
        falseValue = falseValue.trim();
        falseValue = Character.toLowerCase(falseValue.charAt(0)) + falseValue.substring(1);
        String answer = booleanResponse.stringData;
        answer = answer.trim();
        answer = Character.toLowerCase(answer.charAt(0)) + answer.substring(1);
        if (answer.equals(trueValue)) {
            booleanResponse.booleanData = true;
            booleanResponse.state = StringResponse.States.OK;
            return booleanResponse;
        }
        if (answer.equals(falseValue)) {
            booleanResponse.booleanData = false;
            booleanResponse.state = StringResponse.States.OK;
            return booleanResponse;
        }
        booleanResponse.state = StringResponse.States.CANT_CONVERT;
        System.out.println("Can't convert the wrote data to boolean, data: " + booleanResponse.stringData);
        return booleanResponse;
    }
}
