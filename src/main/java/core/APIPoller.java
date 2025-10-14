package core;

import utils.RandomRequestBuilder;
import utils.ParserFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class APIPoller implements Runnable {
    private final BlockingQueue<DataRecord> records;
    private static final HttpClient client = HttpClient.newHttpClient();
    private final RandomRequestBuilder creator;
    private final ParserFactory factory;
    private final Source source;
    private final int delaySeconds;
    private final ScheduledExecutorService executor;

    public APIPoller(Source source, BlockingQueue<DataRecord> queue,
                     int delaySeconds, ScheduledExecutorService executor) {
        this.source = source;
        this.records = queue;
        this.delaySeconds = delaySeconds;
        this.executor = executor;
        creator = new RandomRequestBuilder(source);
        factory = new ParserFactory(source);
    }

    @Override
    public void run() {
        try {
            String json = poll();
            System.out.println(source + " response is taken correctly: " + json.replace('\n', ' '));
            if (json.isBlank() || json.equals("{}")) {
                System.out.println("empty response");
            } else {
                records.put(factory.parse(json));
                System.out.println(source + " ready to sleep");
            }
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        } finally {
        // 2. Планируем новый запуск этого же poller’а
        executor.schedule(() -> executor.submit(this), delaySeconds, TimeUnit.SECONDS);
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
