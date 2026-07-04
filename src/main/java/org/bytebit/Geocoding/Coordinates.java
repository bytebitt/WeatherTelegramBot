package org.bytebit.Geocoding;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Coordinates(double latitude, double longitude) {
}
