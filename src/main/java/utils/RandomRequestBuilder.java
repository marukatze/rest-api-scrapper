package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.Source;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RandomRequestBuilder {
    private final Source source;
    private static final Random random = new Random();
    private List<String> monsters = new ArrayList<>();
    private List<String> spells = new ArrayList<>();
    private static final HttpClient client = HttpClient.newHttpClient();
    private boolean initialized = false;


    public RandomRequestBuilder(Source source) {
        this.source = source;
    }

    private void ensureInitialized() throws IOException, InterruptedException {
        if (!initialized) {
            spells = getEntities("spells");
            monsters = getEntities("monsters");
            initialized = true;
        }
    }

    public String randomizeURL() throws IOException, InterruptedException {
        ensureInitialized();
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

    private List<String> getEntities(String entity) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.dnd5eapi.co/api/2014/" + entity))
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String json = response.body();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode results = root.get("results");

        List<String> entities = new ArrayList<>();
        for (JsonNode node : results) {
            entities.add(node.get("index").asText());
        }

        return entities;
    }
}
