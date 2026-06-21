package org.bytebit.Weather;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class WeatherApiClient {
    public String getWeatherByCoordinates(double latitude, double longitude) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Accept", "application/json")
                .uri(URI.create(buildUrl(latitude, longitude)))
                .timeout(Duration.ofSeconds(10))
                .build();


            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();

            WeatherResponse weatherResponse = mapper.readValue(response.body(), WeatherResponse.class);

            CurrentWeather currentWeather = weatherResponse.currentWeather;

            return "Temperature: "
                    + currentWeather.getTemperature()
                    + "°C\nWind Speed: "
                    + currentWeather.getWindSpeed()
                    + " km/h";
    }

    private String buildUrl(double latitude, double longitude) {
        return "https://api.open-meteo.com/v1/forecast?latitude="
                + latitude + "&longitude="
                + longitude + "&current_weather=true";
    }
}
