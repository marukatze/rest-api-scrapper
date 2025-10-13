package parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.APIParser;
import core.DataRecord;

import java.time.LocalDateTime;

public class DnDParser implements APIParser {
    @Override
    public DataRecord parse(String json) throws JsonProcessingException {
        JsonNode node = new ObjectMapper().readTree(json);
        return new DataRecord(
                "dnd-api",
                node.get("name").asText(),
                node.get("desc").asText(),
                LocalDateTime.now());
    }
}
