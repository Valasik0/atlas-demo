package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class LawAnalyzerService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public LawAnalyzerService(WebClient.Builder webClient) {
        this.webClient = WebClient.builder().build();
    }

    public String analyzeLaw(LawAnalyzerRequest request){
        String prompt = buildPrompt(request);

        Map<String, Object> requestBody = Map.of(
                "contents", new Object[] {
                        Map.of("parts", new Object[] {
                                Map.of("text", prompt)
                        })
                }
        );

        String response = webClient.post()
                .uri(geminiApiUrl)
                .header("Content-Type", "application/json")
                .header("X-goog-api-key", geminiApiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return extractResponseContent(response);
    }

    private String extractResponseContent(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);

            return jsonNode
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text")
                    .asText();
        } catch (Exception e) {
            return "Error processing request: " + e.getMessage();
        }
    }

    private String buildPrompt(LawAnalyzerRequest request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analyzuj následující právní texty. ");
        prompt.append("Máš jedno aktuální znění a několik starších verzí s datem. ");
        prompt.append("Porovnej je a vrať časovou osu změn ve formátu JSON.\n");
        prompt.append("Použij strukturu: [{\"date\": \"YYYY-MM-DD\", \"change\": \"stručný popis změny\"}]\n\n");

        prompt.append("Aktuální znění:\n").append(request.getCurrentText()).append("\n\n");
        prompt.append("Starší verze:\n");

        for (LawVersion version : request.getHistory()) {
            prompt.append("Datum: ").append(version.getDate()).append("\n");
            prompt.append("Text: ").append(version.getText()).append("\n\n");
        }

        return prompt.toString();
    }
}

