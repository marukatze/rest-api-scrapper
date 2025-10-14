package parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import utils.APIParser;
import core.DataRecord;

import java.time.LocalDateTime;

public class CatsFactParser extends APIParser {
    @Override
    public DataRecord parse(String json) throws JsonProcessingException {
        JsonNode node = mapper.readTree(json);
        return new DataRecord(
                "cat-fact-api",
                "cat fact",
                node.get("fact").asText(),
                LocalDateTime.now());
    }
}
