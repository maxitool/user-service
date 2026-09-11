package org.example.console.readers.primitives;

import org.example.console.readers.primitives.responses.DoubleResponse;
import org.example.console.readers.primitives.responses.StringResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DoubleConsoleReaderTest extends AbstractConsoleReaderTest {

    @Test
    public void when_getInt_then_returnResponseWithOkState() {
        provideInput("123");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        assertEquals(StringResponse.States.OK, response.state);
        assertEquals(123, response.doubleData);
    }

    @Test
    public void when_getDoubleWithDot_then_returnResponseWithOkState() {
        provideInput("123.123");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        assertEquals(StringResponse.States.OK, response.state);
        assertEquals(123.123, response.doubleData);
    }

    @Test
    public void when_getEFormatDouble_then_returnResponseWithOkState() {
        provideInput("1.2e123d");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        assertEquals(StringResponse.States.OK, response.state);
        assertEquals(1.2e123d, response.doubleData);
    }

    @Test
    public void when_getDoubleWithComma_then_returnResponseWithOkState() {
        provideInput("123,123");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        assertEquals(StringResponse.States.OK, response.state);
        assertEquals(123.123, response.doubleData);
    }

    @Test
    public void when_getDoubleWithSpaces_then_returnResponseWithOkState() {
        provideInput(" 1 123 . 123 ");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        assertEquals(StringResponse.States.OK, response.state);
        assertEquals(1123.123, response.doubleData);
    }

    @Test
    public void when_getNegativeDouble_then_returnResponseWithOkState() {
        provideInput("-123.123");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        assertEquals(StringResponse.States.OK, response.state);
        assertEquals(-123.123, response.doubleData);
    }

    @Test
    public void when_getBeyondRangeDouble_then_returnResponseWithCantConvertState() {
        provideInput("1.8e50000d");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }

    @Test
    public void when_getNegativeBeyondRangeDouble_then_returnResponseWithCantConvertState() {
        provideInput("-1.8e50000d");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }

    @Test
    public void when_getDoubleWith2Dots_then_returnResponseWithCantConvertState() {
        provideInput("123..123");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }

    @Test
    public void when_getDoubleWith2Commas_then_returnResponseWithCantConvertState() {
        provideInput("123,,123");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }

    @Test
    public void when_getString_then_returnResponseWithCantConvertState() {
        provideInput("data");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }

    @Test
    public void when_getEmpty_then_returnResponseWithBadResponseState() {
        provideInput("");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        assertEquals(StringResponse.States.BAD_RESPONSE, response.state);
    }

    @Test
    public void when_getBoolean_then_returnResponseWithCantConvertState() {
        provideInput("true");
        DoubleResponse response = DoubleConsoleReader.getDoubleData();
        assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }
}
