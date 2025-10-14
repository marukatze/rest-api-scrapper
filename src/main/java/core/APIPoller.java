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
    private final BlockingQueue<DataRecord> records;
    private static final HttpClient client = HttpClient.newHttpClient();
    private final int timeout;
    private final RandomRequestBuilder creator;
    private final ParserFactory factory;
    private Source source;

    public APIPoller(Source source, BlockingQueue<DataRecord> records, int t) {
        this.records = records;
        creator = new RandomRequestBuilder(source);
        timeout = t;
        factory = new ParserFactory(source);
        this.source = source;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                String json = poll();
                System.out.println(source + " response is taken correctly: " + json);
                if (json == null || json.isBlank() || json.equals("{}")) {
                    System.out.println("empty response");
                    continue;
                }
                records.put(factory.parse(json));
                System.out.println(source + " ready to sleep");
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
