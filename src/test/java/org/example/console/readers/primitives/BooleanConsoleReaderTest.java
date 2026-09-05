package org.example.console.readers.primitives;

import org.example.console.readers.primitives.responses.BooleanResponse;
import org.example.console.readers.primitives.responses.StringResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BooleanConsoleReaderTest {
    @AfterEach
    public void closeScanner() {
        StringConsoleReader.closeScanner();
    }

    @AfterAll
    public static void clearSystemIn() {
        System.setIn(StringConsoleReaderTest.SYSTEM_IN_BACKUP);
    }

    @Test
    public void when_getBoolean_then_returnResponseWithOkState() {
        StringConsoleReaderTest.provideInput(" tr ue ");
        BooleanResponse response = BooleanConsoleReader.getBooleanData();
        Assertions.assertEquals(StringResponse.States.OK, response.state);
    }

    @Test
    public void when_getLowerCaseTrue_then_returnResponseWithOkState() {
        StringConsoleReaderTest.provideInput("true");
        BooleanResponse response = BooleanConsoleReader.getBooleanData();
        Assertions.assertEquals(StringResponse.States.OK, response.state);
    }

    @Test
    public void when_getUpperCaseTrue_then_returnResponseWithOkState() {
        StringConsoleReaderTest.provideInput("True");
        BooleanResponse response = BooleanConsoleReader.getBooleanData();
        Assertions.assertEquals(StringResponse.States.OK, response.state);
    }

    @Test
    public void when_getLowerCaseFalse_then_returnResponseWithOkState() {
        StringConsoleReaderTest.provideInput("false");
        BooleanResponse response = BooleanConsoleReader.getBooleanData();
        Assertions.assertEquals(StringResponse.States.OK, response.state);
    }

    @Test
    public void when_getUpperCaseFalse_then_returnResponseWithOkState() {
        StringConsoleReaderTest.provideInput("False");
        BooleanResponse response = BooleanConsoleReader.getBooleanData();
        Assertions.assertEquals(StringResponse.States.OK, response.state);
    }

    @Test
    public void when_getLowerCaseYes_then_returnResponseWithOkState() {
        StringConsoleReaderTest.provideInput("yes");
        BooleanResponse response = BooleanConsoleReader.getBooleanData("yes", "no");
        Assertions.assertEquals(StringResponse.States.OK, response.state);
    }

    @Test
    public void when_getUpperCaseYes_then_returnResponseWithOkState() {
        StringConsoleReaderTest.provideInput("Yes");
        BooleanResponse response = BooleanConsoleReader.getBooleanData("yes", "no");
        Assertions.assertEquals(StringResponse.States.OK, response.state);
    }

    @Test
    public void when_getYesAndSpacesOutside_then_returnResponseWithOkState() {
        StringConsoleReaderTest.provideInput(" yes ");
        BooleanResponse response = BooleanConsoleReader.getBooleanData("yes", "no");
        Assertions.assertEquals(StringResponse.States.OK, response.state);
    }

    @Test
    public void when_getYesAndSpacesInside_then_returnResponseWithСantConvertState() {
        StringConsoleReaderTest.provideInput("y e s");
        BooleanResponse response = BooleanConsoleReader.getBooleanData("yes", "no");
        Assertions.assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }

    @Test
    public void when_getLowerCaseNo_then_returnResponseWithOkState() {
        StringConsoleReaderTest.provideInput("no");
        BooleanResponse response = BooleanConsoleReader.getBooleanData("yes", "no");
        Assertions.assertEquals(StringResponse.States.OK, response.state);
    }

    @Test
    public void when_getUpperCaseNo_then_returnResponseWithOkState() {
        StringConsoleReaderTest.provideInput("No");
        BooleanResponse response = BooleanConsoleReader.getBooleanData("yes", "no");
        Assertions.assertEquals(StringResponse.States.OK, response.state);
    }

    @Test
    public void when_getNoAndSpacesOutside_then_returnResponseWithOkState() {
        StringConsoleReaderTest.provideInput(" no ");
        BooleanResponse response = BooleanConsoleReader.getBooleanData("yes", "no");
        Assertions.assertEquals(StringResponse.States.OK, response.state);
    }

    @Test
    public void when_getNoAndSpacesInside_then_returnResponseWithOkState() {
        StringConsoleReaderTest.provideInput("n o");
        BooleanResponse response = BooleanConsoleReader.getBooleanData("yes", "no");
        Assertions.assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }

    @Test
    public void when_getAnotherValue_then_returnResponseWithCantConvertState() {
        StringConsoleReaderTest.provideInput("another");
        BooleanResponse response = BooleanConsoleReader.getBooleanData("yes", "no");
        Assertions.assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }

    @Test
    public void when_getWithYesNullValuesInArguments_then_returnResponseWithNoneState() {
        StringConsoleReaderTest.provideInput("yes");
        BooleanResponse response = BooleanConsoleReader.getBooleanData(null, null);
        Assertions.assertEquals(StringResponse.States.NONE, response.state);
    }

    @Test
    public void when_getEmptyLine_then_returnResponseWithBadResponseState() {
        StringConsoleReaderTest.provideInput("");
        BooleanResponse response = BooleanConsoleReader.getBooleanData();
        Assertions.assertEquals(StringResponse.States.BAD_RESPONSE, response.state);
    }

    @Test
    public void when_getString_then_returnResponseWithCantConvertState() {
        StringConsoleReaderTest.provideInput("data");
        BooleanResponse response = BooleanConsoleReader.getBooleanData();
        Assertions.assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }

    @Test
    public void when_getInt_then_returnResponseWithCantConvertState() {
        StringConsoleReaderTest.provideInput("123");
        BooleanResponse response = BooleanConsoleReader.getBooleanData();
        Assertions.assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }

    @Test
    public void when_getDouble_then_returnResponseWithCantConvertState() {
        StringConsoleReaderTest.provideInput("123.23");
        BooleanResponse response = BooleanConsoleReader.getBooleanData();
        Assertions.assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }
}
