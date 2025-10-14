package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import core.DataRecord;
import core.Source;
import parsers.JokesParser;
import parsers.CatsFactParser;
import parsers.DnDParser;

@SuppressWarnings("ClassCanBeRecord")
public class ParserFactory {
    private final Source source;

    public ParserFactory(Source source) {
        this.source = source;
    }

    public DataRecord parse(String json) {
        try {
            return switch (source) {
                case CATS -> new CatsFactParser().parse(json);
                case JOKES -> new JokesParser().parse(json);
                case DND -> new DnDParser().parse(json);
            };
        } catch (JsonProcessingException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
}
