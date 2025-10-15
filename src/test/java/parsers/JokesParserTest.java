package parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import core.DataRecord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JokesParserTest {
    @Test
    void parse_shouldReturnDataRecord() throws JsonProcessingException {
        String json = "{\"joke\":\"hello\"}";
        DataRecord record = new JokesParser().parse(json);

        DataRecord expectedRecord = new DataRecord("geek-jokes-api", "Chuck Norris joke", "hello");
        assertEquals(expectedRecord, record);
    }

    @Test
    void parse_shouldThrowJsonProcessingException() {
        String json = "{\"data\":{\"fact\"]}";
        JokesParser parser = new JokesParser();
        assertThrows(JsonProcessingException.class, () -> parser.parse(json));
    }

    @Test
    void parse_shouldThrowNullPointerException() {
        String json = "{\"fact\":[\"data\"]}";
        JokesParser parser = new JokesParser();
        assertThrows(NullPointerException.class, () -> parser.parse(json));
    }
}
