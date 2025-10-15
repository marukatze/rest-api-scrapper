package utils;

import core.DataRecord;
import core.Source;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParserFactoryTest {

    @Test
    void parse_shouldReturnDataRecordForCats() {
        ParserFactory factory = new ParserFactory(Source.CATS);
        String json = "{\"data\": [\"Cats are awesome!\"]}";
        DataRecord record = factory.parse(json);
        assertNotNull(record, "Parser should return a non-null record for valid CATS JSON");
    }

    @Test
    void parse_shouldReturnDataRecordForJokes() {
        ParserFactory factory = new ParserFactory(Source.JOKES);
        String json = "{\"joke\": \"Why do programmers prefer dark mode? Because light attracts bugs.\"}";
        DataRecord record = factory.parse(json);
        assertNotNull(record, "Parser should return a non-null record for valid JOKES JSON");
    }

    @Test
    void parse_shouldReturnDataRecordForDnD() {
        ParserFactory factory = new ParserFactory(Source.DND);
        String json = "{\"index\":\"acid-arrow\",\"name\":\"Acid Arrow\", \"desc\":\"Acid!!!\"}";
        DataRecord record = factory.parse(json);
        assertNotNull(record, "Parser should return a non-null record for valid DND JSON");
    }

    @Test
    void parse_shouldReturnNullOnInvalidJson() {
        ParserFactory factory = new ParserFactory(Source.CATS);
        String invalidJson = "{not a json at all}";
        DataRecord record = factory.parse(invalidJson);
        assertNull(record, "Invalid JSON should return null (after catching JsonProcessingException)");
    }
}
