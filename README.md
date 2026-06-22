# Weather Telegram Bot 🌤️

A simple Telegram bot written in Java that provides current weather information based on a city name using geocoding and weather APIs.

---

## 📌 Features

- Get current weather by city name
- Uses geocoding to convert city → coordinates
- Fetches weather data from Open-Meteo API
- Handles invalid input gracefully
- Async processing (non-blocking bot response)
- Basic error handling for API and parsing issues

---

## ⚙️ Tech Stack

- Java 17+
- Telegram Bots API
- Jackson (JSON parsing)
- Java HttpClient
- Open-Meteo Geocoding API
- Open-Meteo Weather API

---

## 🔄 How it works

1. User sends a city name to the bot
2. Bot sends request to Geocoding API
3. City is converted into latitude/longitude
4. Coordinates are passed to Weather API
5. Bot returns current weather info

---

## 🧠 Architecture

- `GeocodingApiClient` – handles city → coordinates conversion
- `WeatherApiClient` – fetches weather data by coordinates
- `Coordinates` – simple DTO for latitude/longitude
- `WeatherResponse / CurrentWeather` – JSON mapping models
- `MyTelegramBot` – message handling and user interaction

---

## Output Example
```
🌥️Weather in London right now:
Temperature: 27.9°C
Wind Speed: 16.9 km/h

🌡️It's Hot Outside
```

---
