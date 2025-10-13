package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.Source;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;


public class RequestCreator {
    private final Source source;
    private static final Random random = new Random();
    private final List<String> monsters = new CopyOnWriteArrayList<>();
    private final List<String> spells = new CopyOnWriteArrayList<>();
    private static final HttpClient client = HttpClient.newHttpClient();


    public RequestCreator(Source source) {
        this.source = source;
        try {
            getMonsters();
            getSpells();
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public String randomizeURL() {
        return switch (source) {
            case ACHN -> "https://acnhapi.com/v1/villagers/" + random.nextInt(1,390);
            case CATS -> "https://cat-fact.herokuapp.com/" + "facts";
            case DND -> "https://www.dnd5eapi.co/api/2014/" + randomizeDnD();
        };
    }

    private String randomizeDnD() {
        String dataType;
        String requestEntity;
        if (random.nextInt(0, 2) == 0)  {
            dataType = "monsters/";
            requestEntity = monsters.get(random.nextInt(monsters.size()));
        } else {
            dataType = "spells/";
            requestEntity = spells.get(random.nextInt(spells.size()));
        }
        return dataType + requestEntity;
    }

    private void getMonsters() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.dnd5eapi.co/api/2014/monsters"))
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String json = response.body();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode results = root.get("results"); // это массив монстров

        for (JsonNode monster : results) {
            monsters.add(monster.get("index").asText()); // или get("name"), если нужно именно имя
        }
    }

    private void getSpells() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.dnd5eapi.co/api/2014/spells"))
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String json = response.body();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode results = root.get("results"); // это массив монстров

        for (JsonNode monster : results) {
            spells.add(monster.get("index").asText()); // или get("name"), если нужно именно имя
        }
    }
}
