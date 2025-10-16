package utils;

import exceptions.InvalidFileFormat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidFileFormatTest {

    @Test
    void testExceptionHasCorrectMessage() {
        // given
        String message = "Unsupported file format";

        // when
        InvalidFileFormat ex = new InvalidFileFormat(message);

        // then
        assertEquals(message, ex.getMessage(),
                "Message should match the one passed to the constructor");
    }

    @Test
    void testExceptionIsRuntimeException() {
        // when
        InvalidFileFormat ex = new InvalidFileFormat("Test");

        // then
        assertTrue(ex instanceof RuntimeException,
                "InvalidFileFormat should inherit from RuntimeException");
    }

    @Test
    void testThrowingException() {
        // then
        assertThrows(InvalidFileFormat.class, () -> {
            throw new InvalidFileFormat("Wrong file extension");
        }, "Should throw InvalidFileFormat");
    }
}
