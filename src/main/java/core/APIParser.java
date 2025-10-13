package core;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface APIParser {
    public DataRecord parse(String json) throws JsonProcessingException;
}
