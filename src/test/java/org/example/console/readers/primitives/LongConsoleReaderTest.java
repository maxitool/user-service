package org.example.console.readers.primitives;

import org.example.console.readers.primitives.responses.LongResponse;
import org.example.console.readers.primitives.responses.StringResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LongConsoleReaderTest extends AbstractConsoleReaderTest {

    @Test
    public void when_getLong_then_returnResponseWithOkState() {
        provideInput("3000000000000");
        LongResponse response = LongConsoleReader.getLongData();
        assertEquals(StringResponse.States.OK, response.state);
        assertEquals(3000000000000L, response.longData);
    }

    @Test
    public void when_getNegativeLong_then_returnResponseWithOkState() {
        provideInput("-3000000000000");
        LongResponse response = LongConsoleReader.getLongData();
        assertEquals(StringResponse.States.OK, response.state);
        assertEquals(-3000000000000L, response.longData);
    }

    @Test
    public void when_getLongAndSpaces_then_returnResponseWithOkState() {
        provideInput(" 3 000 000 000 000 ");
        LongResponse response = LongConsoleReader.getLongData();
        assertEquals(StringResponse.States.OK, response.state);
        assertEquals(3000000000000L, response.longData);
    }

    @Test
    public void when_getBeyondRangeLong_then_returnResponseWithCantConvertState() {
        provideInput(Long.toString(Long.MAX_VALUE) + '0');
        LongResponse response = LongConsoleReader.getLongData();
        assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }

    @Test
    public void when_getDouble_then_returnResponseWithCantConvertState() {
        provideInput("123.43");
        LongResponse response = LongConsoleReader.getLongData();
        assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }

    @Test
    public void when_getEmpty_then_returnResponseWithBadResponseState() {
        provideInput("");
        LongResponse response = LongConsoleReader.getLongData();
        assertEquals(StringResponse.States.BAD_RESPONSE, response.state);
    }

    @Test
    public void when_getString_then_returnResponseWithCantConvertState() {
        provideInput("data");
        LongResponse response = LongConsoleReader.getLongData();
        assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }

    @Test
    public void when_getBoolean_then_returnResponseWithCantConvertState() {
        provideInput("true");
        LongResponse response = LongConsoleReader.getLongData();
        assertEquals(StringResponse.States.CANT_CONVERT, response.state);
    }
}
