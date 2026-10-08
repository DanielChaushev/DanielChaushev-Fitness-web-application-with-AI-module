package com.danielchaushev.fitnessbackend.service;

import com.danielchaushev.fitnessbackend.dto.gemini.GeminiRequest;
import com.danielchaushev.fitnessbackend.dto.gemini.GeminiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;

@Service
public class GeminiService {

    private final RestClient restClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    public GeminiService() {
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(
                HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build()
        );
        factory.setReadTimeout(Duration.ofSeconds(50));
        this.restClient = RestClient.builder().requestFactory(factory).build();
    }

    public String generateContent(String prompt) {
        int maxAttempts = 3;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return callGemini(prompt);
            } catch (Exception e) {
                if (attempt == maxAttempts) {
                    return "AI услугата е претоварена в момента. Моля, опитайте отново по-късно.";
                }
            }
        }

        return "Неуспешно генериране на план. Опитайте отново.";
    }

    private String callGemini(String prompt) {
        GeminiRequest request = new GeminiRequest(
                List.of(new GeminiRequest.Content(
                        List.of(new GeminiRequest.Part(prompt))
                ))
        );

        GeminiResponse response = restClient.post()
                .uri(apiUrl + "?key=" + apiKey)
                .body(request)
                .retrieve()
                .body(GeminiResponse.class);

        if (response == null || response.getCandidates() == null || response.getCandidates().isEmpty()) {
            return "Неуспешно генериране на план. Опитайте отново.";
        }

        return response.getCandidates().get(0).getContent().getParts().get(0).getText();
    }
}