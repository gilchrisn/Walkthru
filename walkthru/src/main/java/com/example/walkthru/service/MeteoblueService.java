package com.example.walkthru.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MeteoblueService {

    @Value("${api.key}")
    private String apiKey;

    @Value("${meteoblue.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public MeteoblueService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String getCurrentTemperature(double lat, double lon) {
        String url = String.format("%slat=%f&lon=%f&apikey=%s", apiUrl, lat, lon, apiKey);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        try {
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            JsonNode temperatureNode = jsonResponse.path("data_current").path("temperature");
            double temperature = temperatureNode.asDouble();

            String temperatureCategory;
            if (temperature > 32.0) {
                temperatureCategory = "very-hot";
            } else if (temperature > 30.0) {
                temperatureCategory = "hot";
            } else {
                temperatureCategory = "normal";
            }

            return temperatureCategory;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse MeteoBlue API response");
        }
    }
}

