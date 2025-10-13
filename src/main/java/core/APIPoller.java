package core;

import com.fasterxml.jackson.core.JsonProcessingException;
import parsers.ACHNParser;
import parsers.CatsFactParser;
import parsers.DnDParser;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

public class APIPoller implements Runnable{
    private final String url;
    private final Source source;
    HashMap<Source, String> urls = new HashMap<>();
    {
        urls.put(Source.ACHN, "");
        urls.put(Source.CATS, "");
        urls.put(Source.DND, "");
    };
    private final BlockingQueue<DataRecord> records;
    private String json;

    public APIPoller(Source source, BlockingQueue<DataRecord> records) {
        this.source = source;
        url = urls.get(source);
        this.records = records;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            poll();
            parse();
        }
    }

    private void poll() {
        // work with URL
    }

    private void parse() {
        try {
            DataRecord record = switch (source) {
                case CATS -> new CatsFactParser().parse(json);
                case ACHN -> new ACHNParser().parse(json);
                case DND -> new DnDParser().parse(json);
            };
            records.put(record);
        } catch (JsonProcessingException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
