package org.bytebit;

import org.bytebit.Bot.MyTelegramBot;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Main {

    private static final String TELEGRAM_BOT_TOKEN = System.getenv("TELEGRAM_BOT_TOKEN");

    public static void main(String[] args) {
        if (TELEGRAM_BOT_TOKEN == null || TELEGRAM_BOT_TOKEN.isBlank()) {
            throw new RuntimeException("Token not set");
        }

        try (TelegramBotsLongPollingApplication application = new TelegramBotsLongPollingApplication()){
            application.registerBot(TELEGRAM_BOT_TOKEN, new MyTelegramBot());
            Thread.currentThread().join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
