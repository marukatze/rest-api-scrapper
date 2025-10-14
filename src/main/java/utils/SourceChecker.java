package utils;

import core.Source;
import exceptions.InvalidSource;

public class SourceChecker {
    public static Source checkSource(String sourceName) throws InvalidSource {
        if (sourceName.contains("achn")) return Source.ACHN;
        else if (sourceName.contains("dnd")) return Source.DND;
        else if (sourceName.contains("cat")) return Source.CATS;
        else throw new InvalidSource("this source not supported");
    }
}
