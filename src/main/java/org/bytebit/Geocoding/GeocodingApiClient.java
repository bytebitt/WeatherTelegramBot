package org.bytebit.Geocoding;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class GeocodingApiClient {
    public double[] getCoordinatesByCity(String city) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Accept", "application/json")
                .uri(URI.create(buildUrl(city)))
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();

        GeocodingResponse geocodingService = mapper.readValue(response.body(), GeocodingResponse.class);

        double latitude = geocodingService.results.getFirst().getLatitude();
        double longitude = geocodingService.results.getFirst().getLongitude();

        return new double[] {latitude, longitude};
    }

    private String buildUrl(String city) {
        return "https://geocoding-api.open-meteo.com/v1/search?name=" + city + "&count=1&language=en&format=json";
    }
}
