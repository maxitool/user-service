package org.example.console.readers.primitives;

import org.example.console.readers.primitives.responses.StringResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringConsoleReaderTest extends AbstractConsoleReaderTest {

    @Test
    public void when_getEmpty_then_returnResponseWithBadResponseState() {
        provideInput("");
        StringResponse response = StringConsoleReader.getStringData();
        assertEquals(StringResponse.States.BAD_RESPONSE, response.state);
    }

    @Test
    public void when_getString_then_returnResponseWithOkState() {
        provideInput("data");
        StringResponse response = StringConsoleReader.getStringData();
        assertEquals(StringResponse.States.OK, response.state);
        assertEquals("data", response.stringData);
    }

    @Test
    public void when_getBackCommand_then_returnResponseWithBackCommandState() {
        provideInput(StringConsoleReader.GO_BACK_COMMAND);
        StringResponse response = StringConsoleReader.getStringData();
        assertEquals(StringResponse.States.BACK_COMMAND, response.state);
    }

    @Test
    public void when_getStringAndSpacesOutside_then_returnResponseWithBackCommandState() {
        provideInput(' ' + StringConsoleReader.GO_BACK_COMMAND + ' ');
        StringResponse response = StringConsoleReader.getStringData();
        assertEquals(StringResponse.States.BACK_COMMAND, response.state);
    }
}
