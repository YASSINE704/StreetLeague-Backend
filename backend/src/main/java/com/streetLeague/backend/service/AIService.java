package com.streetLeague.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AIService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${ollama.url}")
    private String ollamaUrl;

    @Value("${ollama.model}")
    private String ollamaModel;

    // 🔥 1. SMART REPLY (FORUM)
    public String generateSmartReply(String postContent) {

        String prompt = """
        You are an assistant for the StreetLeague sports application.
        You ONLY help users with this application's features: Marketplace (achat/vente d'équipements sportifs), Training (programmes d'entraînement), Matches, Tournaments, and Forum.
        You have NO knowledge about real-world teams, players, leagues, competitions, or sports news.
        Never mention real clubs (e.g. AS Monaco, FC Nantes, RC Lens), real players, or any external information.
        Base your reply EXCLUSIVELY on the text of the post below.
        If the user asks about something outside the application, politely redirect them to use StreetLeague's features.
        If you lack information, say so honestly and suggest asking for more details within the app.

        Post:
        %s
        """.formatted(postContent);

        String response = callOllama(prompt);

        return extractResponse(response);
    }

    // 🛒 2. SMART SEARCH (MARKETPLACE)
    public String smartSearch(String query) {

        String prompt = """
        You are a search filter extractor for a sports marketplace.
        Given a user query, return ONLY valid JSON with these optional fields.
        Only use categories and subcategories from the predefined list below. Never invent new values.
        {
          "category": "football" | "basketball" | "volleyball" | "padel" | "tennis" | "running" | "fitness" | null,
          "subcategory": "shoes" | "pull" | "shorts" | "equipment" | "accessories" | null,
          "minPrice": number | null,
          "maxPrice": number | null,
          "searchTerm": string | null
        }

        User query: "%s"

        JSON:
        """.formatted(query);

        String response = callOllama(prompt);

        return extractResponse(response);
    }

    // 🔧 CORE OLLAMA CALL
    private String callOllama(String prompt) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("model", ollamaModel);
        body.put("prompt", prompt);
        body.put("stream", false);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                ollamaUrl + "/api/generate",
                request,
                String.class
        );

        return response.getBody();
    }

    // 🧠 extract ONLY AI text
    private String extractResponse(String json) {
        try {
            JsonNode node = mapper.readTree(json);
            return node.get("response").asText();
        } catch (Exception e) {
            return "Error parsing AI response";
        }
    }
}