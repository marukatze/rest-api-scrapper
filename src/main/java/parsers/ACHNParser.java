package parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import utils.APIParser;
import core.DataRecord;

import java.time.LocalDateTime;

public class ACHNParser extends APIParser {
    @Override
    public DataRecord parse(String json) throws JsonProcessingException {
        JsonNode node = mapper.readTree(json);
        return new DataRecord(
                "achn-api",
                node.get("name").get("name-USen").asText(),
                "personality: " + node.get("personality").asText() +
                "\nbirthday: " + node.get("birthday").asText() +
                "\nspecies: " + node.get("species").asText() +
                "\ngender: " + node.get("gender").asText(),
                LocalDateTime.now());
    }
}
