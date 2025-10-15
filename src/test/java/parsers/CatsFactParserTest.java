package parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import core.DataRecord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CatsFactParserTest {

    @Test
    void parse_shouldReturnDataRecord() throws JsonProcessingException {
        String json = "{\"data\":[\"fact\"]}";
        DataRecord record = new CatsFactParser().parse(json);

        DataRecord expectedRecord = new DataRecord("meowfacts-api", "cat-fact", "fact");
        assertEquals(expectedRecord, record);
    }
}
