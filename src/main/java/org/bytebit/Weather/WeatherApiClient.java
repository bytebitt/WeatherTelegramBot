package org.bytebit.Weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class WeatherApiClient {
    /**
     * Retrieves weather information for a location using its geographic coordinates.
     *
     * <p>
     * This method sends an HTTP request to the Weather API with the provided
     * latitude and longitude, parses the JSON response, and converts the weather
     * data into a readable string format.
     * </p>
     *
     * @param latitude the latitude coordinate of the location
     * @param longitude the longitude coordinate of the location
     * @return formatted weather information as a string,
     *         or {@code null} if the API request was unsuccessful
     * @throws IOException if an I/O error occurs during the HTTP request or JSON parsing
     * @throws InterruptedException if the HTTP request is interrupted
     */
    public String getWeatherByCoordinates(double latitude, double longitude) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Accept", "application/json")
                .uri(URI.create(buildUrl(latitude, longitude)))
                .timeout(Duration.ofSeconds(20))
                .build();


        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            return null;
        } else {
            ObjectMapper mapper = new ObjectMapper();

            WeatherResponse weatherResponse = mapper.readValue(response.body(), WeatherResponse.class);

            String weather = getString(weatherResponse);

            return weather;
        }
    }

    private static String getString(WeatherResponse weatherResponse) {
        CurrentWeather currentWeather = weatherResponse.currentWeather;

        boolean isHotWeather = currentWeather.temperature() > 25;
        boolean isColdWeather = currentWeather.temperature() < 10;

        String weather = "Temperature: "
                + currentWeather.temperature()
                + "°C\nWind Speed: "
                + currentWeather.temperature()
                + " km/h";

        if (isHotWeather) {
            weather += "\n\n🌡️It's Hot Outside";
        } else if (isColdWeather) {
            weather += "\n\n🥶It's Cold Outside";
        } else {
            weather += "\n\n👌It's Mild Weather";
        }
        return weather;
    }

    private String buildUrl(double latitude, double longitude) {
        return "https://api.open-meteo.com/v1/forecast?latitude="
                + latitude + "&longitude="
                + longitude + "&current_weather=true";
    }
}
