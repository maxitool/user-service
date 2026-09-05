package org.example.console.readers.primitives;

import org.example.console.readers.primitives.responses.StringResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class StringConsoleReaderTest {
    public static final InputStream SYSTEM_IN_BACKUP = System.in;

    public static void provideInput(String data){
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    @AfterEach
    public void closeScanner() {
        StringConsoleReader.closeScanner();
    }

    @AfterAll
    public static void clearSystemIn() {
        System.setIn(StringConsoleReaderTest.SYSTEM_IN_BACKUP);
    }

    @Test
    public void when_getEmpty_then_returnResponseWithBadResponseState() {
        provideInput("");
        StringResponse response = StringConsoleReader.getStringData();
        Assertions.assertEquals(StringResponse.States.BAD_RESPONSE, response.state);
    }

    @Test
    public void when_getString_then_returnResponseWithOkState() {
        provideInput("data");
        StringResponse response = StringConsoleReader.getStringData();
        Assertions.assertEquals(StringResponse.States.OK, response.state);
    }

    @Test
    public void when_getBackCommand_then_returnResponseWithBackCommandState() {
        provideInput("back");
        StringResponse response = StringConsoleReader.getStringData();
        Assertions.assertEquals(StringResponse.States.BACK_COMMAND, response.state);
    }

    @Test
    public void when_getStringAndSpacesInside_then_returnResponseWithOkState() {
        provideInput("b a c k");
        StringResponse response = StringConsoleReader.getStringData();
        Assertions.assertEquals(StringResponse.States.OK, response.state);
    }

    @Test
    public void when_getStringAndSpacesOutside_then_returnResponseWithBackCommandState() {
        provideInput(" back ");
        StringResponse response = StringConsoleReader.getStringData();
        Assertions.assertEquals(StringResponse.States.BACK_COMMAND, response.state);
    }
}
