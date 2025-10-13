package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import core.DataRecord;
import core.Source;
import parsers.ACHNParser;
import parsers.CatsFactParser;
import parsers.DnDParser;

public class ParserFactory {
    private Source source;

    public ParserFactory(Source source) {
        this.source = source;
    }

    public DataRecord parse(String json) {
        try {
            return switch (source) {
                case CATS -> new CatsFactParser().parse(json);
                case ACHN -> new ACHNParser().parse(json);
                case DND -> new DnDParser().parse(json);
            };
        } catch (JsonProcessingException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
}
