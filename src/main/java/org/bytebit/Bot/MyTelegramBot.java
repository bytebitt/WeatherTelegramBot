package org.bytebit.Bot;

import org.bytebit.Geocoding.GeocodingApiClient;
import org.bytebit.Weather.CurrentWeather;
import org.bytebit.Weather.WeatherApiClient;
import org.checkerframework.checker.units.qual.C;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.IOException;

public class MyTelegramBot implements LongPollingSingleThreadUpdateConsumer {

    private static final String TELEGRAM_BOT_TOKEN = System.getenv("TELEGRAM_BOT_TOKEN");
    private static final GeocodingApiClient geocodingApiClient = new GeocodingApiClient();
    private static final WeatherApiClient weatherApiClient = new WeatherApiClient();
    private final TelegramClient telegramClient = new OkHttpTelegramClient(TELEGRAM_BOT_TOKEN);

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();

            if (messageText == null || messageText.isBlank()) {
                sendMessage(chatId,"Enter the city name clearly");
            } else if (messageText.equals("/start")) {
                sendMessage(chatId, """
                        👋Hi! I'm Weather Telegram Bot by bytebit.\n
                        Enter a city, and I'll send weather information for the city.
                        """);
            } else if (messageText.equals("/help")) {
                sendMessage(chatId, """
                        🤖This bot is designed to send weather information.\n
                        Send the name of the city, and the bot will send temperature and wind speed.\n
                        If the title consists of two words, use a hyphen.
                        """);
            } else {
                try {
                    double[] coordinates = geocodingApiClient.getCoordinatesByCity(messageText);

                    if (coordinates == null) {
                        sendMessage(chatId, "Invalid input.");
                        return;
                    }

                    double latitude = coordinates[0];
                    double longitude = coordinates[1];

                    String weather = weatherApiClient.getWeatherByCoordinates(latitude, longitude);
                    sendMessage(chatId, weather);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = SendMessage
                .builder()
                .chatId(chatId)
                .text(text)
                .build();

        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Telegram Api Exception: ", e);
        }
    }
}
