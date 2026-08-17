package org.example.ai;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class OllamaClient {

    // ollama local pe hi chalta hai, default port 11434
//    private static final String DEFAULT_BASE_URL = "http://localhost:11434";
//    private static final String DEFAULT_BASE_URL = "http://host.docker.internal:11434";
    private static final String DEFAULT_BASE_URL =
            "http://ollama:11434";
    private static final String DEFAULT_MODEL = "gemma4:latest";

    private final String baseUrl;
    private final String model;
    private final Gson gson = new Gson();

    // client ek hi baar banao, har request pe naya banana mehnga padta hai
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public OllamaClient() {
        this(DEFAULT_BASE_URL, DEFAULT_MODEL);
    }

    public OllamaClient(String model) {
        this(DEFAULT_BASE_URL, model);
    }

    public OllamaClient(String baseUrl, String model) {
        this.baseUrl = baseUrl;
        this.model = model;
    }

    // PromptBuilder se bana prompt yahan aayega, model ka plain text jawab wapas jayega
    public String generate(String prompt) throws IOException, InterruptedException {

        if (prompt == null || prompt.isBlank()) {
            throw new IllegalArgumentException("prompt is empty");
        }

        // body Gson se bana rahe hain, warna prompt ke quotes/newlines JSON tod dete hain
        JsonObject body = new JsonObject();
        body.addProperty("model", model);
        body.addProperty("prompt", prompt);

        // stream false matlab poora jawab ek hi JSON mein aayega, chunks handle nahi karne padenge
        body.addProperty("stream", false);

        // temperature 0 taki har baar same JSON aaye, creativity yahan nahi chahiye
        JsonObject options = new JsonObject();
        options.addProperty("temperature", 0);
        body.add("options", options);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/generate"))
                .header("Content-Type", "application/json")
                // pehli call par model RAM mein load hota hai, tab tak 2 min kam pad jate hain
                .timeout(Duration.ofMinutes(5))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(body), StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        if (response.statusCode() != 200) {
            throw new IOException("Ollama call failed with status "
                    + response.statusCode() + ": " + response.body());
        }

        JsonObject responseJson = gson.fromJson(response.body(), JsonObject.class);

        if (responseJson == null || !responseJson.has("response")) {
            throw new IOException("Unexpected response from Ollama: " + response.body());
        }

        return responseJson.get("response").getAsString().strip();
    }

    // model ko JSON hi mangana hai par kabhi kabhi ```json ... ``` mein lapet deta hai, vo hata dete hain
    public String generateJson(String prompt) throws IOException, InterruptedException {
        return stripCodeFences(generate(prompt));
    }

    private String stripCodeFences(String text) {

        String cleaned = text.strip();

        if (!cleaned.startsWith("```")) {
            return cleaned;
        }

        // pehli line ``` ya ```json hoti hai, use poori hata do
        int firstNewLine = cleaned.indexOf('\n');

        if (firstNewLine != -1) {
            cleaned = cleaned.substring(firstNewLine + 1);
        }

        int closingFence = cleaned.lastIndexOf("```");

        if (closingFence != -1) {
            cleaned = cleaned.substring(0, closingFence);
        }

        return cleaned.strip();
    }

    // ollama chal bhi raha hai ya nahi, ye check karne ke liye
    public boolean isAvailable() {

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/api/tags"))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;

        } catch (IOException | InterruptedException e) {

            return false;
        }
    }

    public String getModel() {
        return model;
    }
}
