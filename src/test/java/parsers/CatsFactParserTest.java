package parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import core.DataRecord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CatsFactParserTest {

    @Test
    void parse_shouldReturnDataRecord() throws JsonProcessingException {
        String json = "{\"data\":[\"fact\"]}";
        DataRecord record = new CatsFactParser().parse(json);

        DataRecord expectedRecord = new DataRecord("meowfacts-api", "cat-fact", "fact");
        assertEquals(expectedRecord, record);
    }

    @Test
    void parse_shouldThrowJsonProcessingException() throws JsonProcessingException {
        String json = "{\"data\":{\"fact\"]}";
        CatsFactParser parser = new CatsFactParser();
        assertThrows(JsonProcessingException.class, () -> parser.parse(json));
    }
}
