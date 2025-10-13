package core;

import utils.RandomRequestBuilder;
import utils.ParserFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class APIPoller implements Runnable {
    private final Source source;
    private final BlockingQueue<DataRecord> records;
    private static final HttpClient client = HttpClient.newHttpClient();
    private final int timeout;
    private final RandomRequestBuilder creator;

    public APIPoller(Source source, BlockingQueue<DataRecord> records, int t) {
        this.source = source;
        this.records = records;
        creator = new RandomRequestBuilder(source);
        timeout = t;
    }

    @Override
    public void run() {
        ParserFactory factory = new ParserFactory(source);
        try {
            while (!Thread.currentThread().isInterrupted()) {
                String json = poll();
                records.put(factory.parse(json));
                TimeUnit.SECONDS.sleep(timeout);
            }
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String poll() throws IOException, InterruptedException {
        String url = creator.randomizeURL();
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
}
