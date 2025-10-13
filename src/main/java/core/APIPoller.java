package core;

import com.fasterxml.jackson.core.JsonProcessingException;
import parsers.ACHNParser;
import parsers.CatsFactParser;
import parsers.DnDParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class APIPoller implements Runnable {
    private final String url;
    private final Source source;
    private final HashMap<Source, String> urls = new HashMap<>();

    {
        urls.put(Source.ACHN, "https://acnhapi.com/v1/villagers/");
        urls.put(Source.CATS, "https://cat-fact.herokuapp.com/");
        urls.put(Source.DND, "https://www.dnd5eapi.co/api/2014/monsters/");
    }

    private final BlockingQueue<DataRecord> records;
    private static final HttpClient client = HttpClient.newHttpClient();
    private final int timeout;

    public APIPoller(Source source, BlockingQueue<DataRecord> records, int t) {
        this.source = source;
        url = urls.get(source);
        this.records = records;
        timeout = t;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                String json = poll();
                parse(json);
                TimeUnit.SECONDS.sleep(timeout);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String poll() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                // kind of logging
                return "{}";
            }

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            // kind of logging
            return "{}";
        }
    }

    private void parse(String json) {
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
