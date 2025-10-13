package core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class APIParser {
    private static final ObjectMapper mapper = new ObjectMapper();
    public abstract DataRecord parse(String json) throws JsonProcessingException;
}
