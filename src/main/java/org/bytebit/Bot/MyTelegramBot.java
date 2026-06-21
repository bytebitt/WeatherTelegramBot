package org.bytebit.Bot;

import org.bytebit.Geocoding.Coordinates;
import org.bytebit.Geocoding.GeocodingApiClient;
import org.bytebit.Weather.WeatherApiClient;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.IOException;

public class MyTelegramBot implements LongPollingSingleThreadUpdateConsumer {

    private static final String TELEGRAM_BOT_TOKEN = System.getenv("TELEGRAM_BOT_TOKEN");
    private static final GeocodingApiClient geocodingApiClient = new GeocodingApiClient();
    private static final WeatherApiClient weatherApiClient = new WeatherApiClient();
    private static final TelegramClient telegramClient = new OkHttpTelegramClient(TELEGRAM_BOT_TOKEN);

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();
            SendChatAction chatAction = SendChatAction.builder()
                    .chatId(chatId)
                    .action("typing")
                    .build();
            try {
                if (messageText == null || messageText.isBlank()) {
                    telegramClient.execute(chatAction);
                    sendMessage(chatId, "Enter the city name clearly");
                } else if (messageText.equals("/start")) {
                    telegramClient.execute(chatAction);
                    sendMessage(chatId, """
                            👋Hi! I'm Weather Telegram Bot by bytebit.\n
                            Enter a city, and I'll send weather information for the city.
                            """);
                } else if (messageText.equals("/help")) {
                    telegramClient.execute(chatAction);
                    sendMessage(chatId, """
                            🤖This bot is designed to send weather information.\n
                            Send the name of the city, and the bot will send temperature and wind speed.\n
                            If the title consists of two words, use a hyphen.
                            """);
                } else {
                    SendMessage loadingMessageBuilder = SendMessage.builder()
                            .chatId(chatId)
                            .text("⚙️Loading Weather Information...")
                            .build();

                    Message loadingMessage = telegramClient.execute(loadingMessageBuilder);

                    telegramClient.execute(chatAction);

                    new Thread(() -> {
                        try {
                            Coordinates coordinates = geocodingApiClient.getCoordinatesByCity(messageText);

                            if (coordinates == null) {
                                editMessage(chatId, loadingMessage, "❌Invalid Input.");
                                return;
                            }

                            double latitude = coordinates.getLatitude();
                            double longitude = coordinates.getLongitude();

                            String weather = weatherApiClient.getWeatherByCoordinates(latitude, longitude);

                            editMessage(chatId, loadingMessage, weather);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }).start();
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
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

    private void editMessage(long chatId, Message messageId, String text) {
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId.getMessageId())
                .text(text)
                .build();

        try {
            telegramClient.execute(editMessageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
