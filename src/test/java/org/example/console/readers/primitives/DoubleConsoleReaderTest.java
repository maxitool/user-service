package org.example.console.readers.primitives;

import org.example.console.readers.primitives.responses.DoubleResponse;
import org.example.console.readers.primitives.responses.StringResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DoubleConsoleReaderTest {
    @AfterEach
    public void closeScanner() {
        StringConsoleReader.closeScanner();
    }

    @AfterAll
    public static void clearSystemIn() {
        System.setIn(StringConsoleReaderTest.SYSTEM_IN_BACKUP);
    }

    @Test
    public void when_getInt_then_returnResponseWithOkState() {
        StringConsoleReaderTest.provideInput("123");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        Assertions.assertEquals(StringResponse.States.OK, response.state);
    }

    @Test
    public void when_getDoubleWithDot_then_returnResponseWithOkState() {
        StringConsoleReaderTest.provideInput("123.123");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        Assertions.assertEquals(StringResponse.States.OK, response.state);
    }

    @Test
    public void when_getEFormatDouble_then_returnResponseWithOkState() {
        StringConsoleReaderTest.provideInput("1.2e123d");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        Assertions.assertEquals(StringResponse.States.OK, response.state);
    }

    @Test
    public void when_getDoubleWithComma_then_returnResponseWithOkState() {
        StringConsoleReaderTest.provideInput("123,123");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        Assertions.assertEquals(StringResponse.States.OK, response.state);
    }

    @Test
    public void when_getDoubleWithSpaces_then_returnResponseWithOkState() {
        StringConsoleReaderTest.provideInput(" 1 123 . 123 ");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        Assertions.assertEquals(StringResponse.States.OK, response.state);
    }

    @Test
    public void when_getNegativeDouble_then_returnResponseWithOkState() {
        StringConsoleReaderTest.provideInput("-123.123");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        Assertions.assertEquals(StringResponse.States.OK, response.state);
    }

    @Test
    public void when_getBeyondRangeDouble_then_returnResponseWithCantConvertState() {
        StringConsoleReaderTest.provideInput("1.8e50000d");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        Assertions.assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }

    @Test
    public void when_getNegativeBeyondRangeDouble_then_returnResponseWithCantConvertState() {
        StringConsoleReaderTest.provideInput("-1.8e50000d");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        Assertions.assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }

    @Test
    public void when_getDoubleWith2Dots_then_returnResponseWithCantConvertState() {
        StringConsoleReaderTest.provideInput("123..123");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        Assertions.assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }

    @Test
    public void when_getDoubleWith2Commas_then_returnResponseWithCantConvertState() {
        StringConsoleReaderTest.provideInput("123,,123");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        Assertions.assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }

    @Test
    public void when_getString_then_returnResponseWithCantConvertState() {
        StringConsoleReaderTest.provideInput("data");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        Assertions.assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }

    @Test
    public void when_getEmpty_then_returnResponseWithBadResponseState() {
        StringConsoleReaderTest.provideInput("");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        Assertions.assertEquals(StringResponse.States.BAD_RESPONSE, response.state);
    }

    @Test
    public void when_getBoolean_then_returnResponseWithCantConvertState() {
        StringConsoleReaderTest.provideInput("true");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        Assertions.assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }
}
