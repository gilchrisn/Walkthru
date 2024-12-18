package com.example.walkthru.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RainfallService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public RainfallService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public String getCurrentRainfall(String stationId) {
        String url = "https://api.data.gov.sg/v1/environment/rainfall";
        String response = restTemplate.getForObject(url, String.class);

        try {
            JsonNode jsonResponse = objectMapper.readTree(response);
            JsonNode items = jsonResponse.path("items");
            if (items.isArray() && items.size() > 0) {
                JsonNode readings = items.get(0).path("readings");
                if (readings.isArray()) {
                    for (JsonNode reading : readings) {
                        if (reading.path("station_id").asText().equals(stationId)) {
                            double rainfall = reading.path("value").asDouble();

                            String rainfallCategory;
                            if (rainfall > 0.5) {
                                rainfallCategory = "rain";
                            } else {
                                rainfallCategory = "no-rain";
                            }
                            return rainfallCategory;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse rainfall data");
        }

        throw new RuntimeException("Device ID not found");
    }
}
