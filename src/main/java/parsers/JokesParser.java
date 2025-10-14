package parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import utils.APIParser;
import core.DataRecord;



public class JokesParser extends APIParser {
    @Override
    public DataRecord parse(String json) throws JsonProcessingException {
        JsonNode node = mapper.readTree(json);
        return new DataRecord(
                "geek-jokes-api",
                "Chuck Norris joke",
                node.get("joke").asText());
    }
}
