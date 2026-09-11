package org.example.console.readers.primitives;

import org.junit.Ignore;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Ignore
public abstract class AbstractConsoleReaderTest {
    public static final InputStream SYSTEM_IN_BACKUP = System.in;

    @AfterEach
    public void closeScanner() {
        StringConsoleReader.closeScanner();
    }

    @AfterAll
    public static void clearSystemIn() {
        System.setIn(SYSTEM_IN_BACKUP);
    }

    protected static void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }
}
