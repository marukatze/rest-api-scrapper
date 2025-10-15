package parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import core.DataRecord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DnDParserTest {

    @Test
    void parse_shouldReturnDataRecord() throws JsonProcessingException {
        String json = "{\"name\":\"Troll\",\"desc\":\"rawr\"}";
        DataRecord record = new DnDParser().parse(json);

        DataRecord expectedRecord = new DataRecord("dnd-api", "Troll", "\"rawr\"");
        assertEquals(expectedRecord, record);
    }

    @Test
    void parse_shouldThrowJsonProcessingException() {
        String json = "{\"data\":{\"fact\"]}";
        DnDParser parser = new DnDParser();
        assertThrows(JsonProcessingException.class, () -> parser.parse(json));
    }

    @Test
    void parse_shouldThrowNullPointerException() {
        String json = "{\"fact\":[\"data\"]}";
        DnDParser parser = new DnDParser();
        assertThrows(NullPointerException.class, () -> parser.parse(json));
    }
}
