package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.DataRecord;

public abstract class APIParser {
    private static final ObjectMapper mapper = new ObjectMapper();
    public abstract DataRecord parse(String json) throws JsonProcessingException;
}
