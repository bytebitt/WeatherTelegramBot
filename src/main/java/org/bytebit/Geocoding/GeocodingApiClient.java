package org.bytebit.Geocoding;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class GeocodingApiClient {
    public Coordinates getCoordinatesByCity(String city) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Accept", "application/json")
                .uri(URI.create(buildUrl(city)))
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            return null;
        } else {
            ObjectMapper mapper = new ObjectMapper();

            GeocodingResponse geocodingResponse = mapper.readValue(response.body(), GeocodingResponse.class);

            if (geocodingResponse == null ||
                    geocodingResponse.results == null||
                    geocodingResponse.results.isEmpty()) {
                return null;
            }

            double latitude = geocodingResponse.results.getFirst().getLatitude();
            double longitude = geocodingResponse.results.getFirst().getLongitude();

            return new Coordinates(latitude, longitude);
        }
    }

    private String buildUrl(String city) {
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
        return "https://geocoding-api.open-meteo.com/v1/search?name=" + encodedCity + "&count=1&language=en&format=json";
    }
}
