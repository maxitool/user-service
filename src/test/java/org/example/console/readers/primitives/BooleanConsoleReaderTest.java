package org.example.console.readers.primitives;

import org.example.console.readers.primitives.responses.BooleanResponse;
import org.example.console.readers.primitives.responses.StringResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BooleanConsoleReaderTest extends AbstractConsoleReaderTest {

    @Test
    public void when_getBoolean_then_returnResponseWithOkState() {
        provideInput(" tr ue ");
        BooleanResponse response = BooleanConsoleReader.getBooleanData();
        assertEquals(StringResponse.States.OK, response.state);
        assertTrue(response.booleanData);
    }

    @Test
    public void when_getLowerCaseTrue_then_returnResponseWithOkState() {
        provideInput("true");
        BooleanResponse response = BooleanConsoleReader.getBooleanData();
        assertEquals(StringResponse.States.OK, response.state);
        assertTrue(response.booleanData);
    }

    @Test
    public void when_getUpperCaseTrue_then_returnResponseWithOkState() {
        provideInput("True");
        BooleanResponse response = BooleanConsoleReader.getBooleanData();
        assertEquals(StringResponse.States.OK, response.state);
        assertTrue(response.booleanData);
    }

    @Test
    public void when_getLowerCaseFalse_then_returnResponseWithOkState() {
        provideInput("false");
        BooleanResponse response = BooleanConsoleReader.getBooleanData();
        assertEquals(StringResponse.States.OK, response.state);
        assertFalse(response.booleanData);
    }

    @Test
    public void when_getUpperCaseFalse_then_returnResponseWithOkState() {
        provideInput("False");
        BooleanResponse response = BooleanConsoleReader.getBooleanData();
        assertEquals(StringResponse.States.OK, response.state);
        assertFalse(response.booleanData);
    }

    @Test
    public void when_getLowerCaseYes_then_returnResponseWithOkState() {
        provideInput("yes");
        BooleanResponse response = BooleanConsoleReader.getBooleanData("yes", "no");
        assertEquals(StringResponse.States.OK, response.state);
        assertTrue(response.booleanData);
    }

    @Test
    public void when_getUpperCaseYes_then_returnResponseWithOkState() {
        provideInput("Yes");
        BooleanResponse response = BooleanConsoleReader.getBooleanData("yes", "no");
        assertEquals(StringResponse.States.OK, response.state);
        assertTrue(response.booleanData);
    }

    @Test
    public void when_getYesAndSpacesOutside_then_returnResponseWithOkState() {
        provideInput(" yes ");
        BooleanResponse response = BooleanConsoleReader.getBooleanData("yes", "no");
        assertEquals(StringResponse.States.OK, response.state);
        assertTrue(response.booleanData);
    }

    @Test
    public void when_getYesAndSpacesInside_then_returnResponseWithСantConvertState() {
        provideInput("y e s");
        BooleanResponse response = BooleanConsoleReader.getBooleanData("yes", "no");
        assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }

    @Test
    public void when_getLowerCaseNo_then_returnResponseWithOkState() {
        provideInput("no");
        BooleanResponse response = BooleanConsoleReader.getBooleanData("yes", "no");
        assertEquals(StringResponse.States.OK, response.state);
        assertFalse(response.booleanData);
    }

    @Test
    public void when_getUpperCaseNo_then_returnResponseWithOkState() {
        provideInput("No");
        BooleanResponse response = BooleanConsoleReader.getBooleanData("yes", "no");
        assertEquals(StringResponse.States.OK, response.state);
        assertFalse(response.booleanData);
    }

    @Test
    public void when_getNoAndSpacesOutside_then_returnResponseWithOkState() {
        provideInput(" no ");
        BooleanResponse response = BooleanConsoleReader.getBooleanData("yes", "no");
        assertEquals(StringResponse.States.OK, response.state);
        assertFalse(response.booleanData);
    }

    @Test
    public void when_getNoAndSpacesInside_then_returnResponseWithOkState() {
        provideInput("n o");
        BooleanResponse response = BooleanConsoleReader.getBooleanData("yes", "no");
        assertEquals(StringResponse.States.CANT_CONVERT, response.state);
        assertFalse(response.booleanData);
    }

    @Test
    public void when_getAnotherValue_then_returnResponseWithCantConvertState() {
        provideInput("another");
        BooleanResponse response = BooleanConsoleReader.getBooleanData("yes", "no");
        assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }

    @Test
    public void when_getWithYesNullValuesInArguments_then_returnResponseWithNoneState() {
        provideInput("yes");
        BooleanResponse response = BooleanConsoleReader.getBooleanData(null, null);
        assertEquals(StringResponse.States.NONE, response.state);
    }

    @Test
    public void when_getEmptyLine_then_returnResponseWithBadResponseState() {
        provideInput("");
        BooleanResponse response = BooleanConsoleReader.getBooleanData();
        assertEquals(StringResponse.States.BAD_RESPONSE, response.state);
    }

    @Test
    public void when_getString_then_returnResponseWithCantConvertState() {
        provideInput("data");
        BooleanResponse response = BooleanConsoleReader.getBooleanData();
        assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }

    @Test
    public void when_getInt_then_returnResponseWithCantConvertState() {
        provideInput("123");
        BooleanResponse response = BooleanConsoleReader.getBooleanData();
        assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }

    @Test
    public void when_getDouble_then_returnResponseWithCantConvertState() {
        provideInput("123.23");
        BooleanResponse response = BooleanConsoleReader.getBooleanData();
        assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }
}
