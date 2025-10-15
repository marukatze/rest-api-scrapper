package utils;

import core.Source;
import exceptions.InvalidSource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static utils.SourceChecker.checkSource;

public class SourceCheckerTest {
    @Test
    void checkSource_shouldReturnJOKESWhenSourceIsJoke() {
        String sourceName = "jokes";
        Source source = checkSource(sourceName);
        assertEquals(Source.JOKES, source);
    }

    @Test
    void checkSource_shouldReturnDNDWhenSourceIsDnd() {
        String sourceName = "dnd-api";
        Source source = checkSource(sourceName);
        assertEquals(Source.DND, source);
    }

    @Test
    void checkSource_shouldReturnCATSWhenSourceIsCat() {
        String sourceName = "cats";
        Source source = checkSource(sourceName);
        assertEquals(Source.CATS, source);
    }

    @Test
    void checkSource_shouldThrowInvalidSource() {
        String sourceName = "polytech";
        assertThrows(InvalidSource.class, () -> checkSource(sourceName));
    }
}
