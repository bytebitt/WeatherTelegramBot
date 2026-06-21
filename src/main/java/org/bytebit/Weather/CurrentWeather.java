package org.bytebit.Weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentWeather {
    private double temperature;
    @JsonProperty("windspeed")
    private double windSpeed;

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

//    @Override
//    public String toString() {
//        return "Current Weather:" +
//                "\nTemperature=" + temperature +
//                "\nWind Speed=" + windSpeed;
//    }
}
