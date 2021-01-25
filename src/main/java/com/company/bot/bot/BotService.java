package com.company.bot.bot;

import com.company.bot.model.City;
import com.company.bot.service.CityService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.company.bot.util.BotConstants.*;

@Component
public class BotService extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    private final CityService cityService;

    public BotService(CityService cityService) {
        this.cityService = cityService;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            switch (update.getMessage().getText()) {
                case "/start":
                    sendMessageWithButtons(update.getMessage().getChatId(), GREETING);
                    break;
                case "/list":
                    sendMessageWithButtons(update.getMessage().getChatId(), NEW_INFO);
                    break;
                case "/help":
                    execute(sendTextMessage(update.getMessage().getChatId(), HELP));
                    break;
                default:
                    execute(sendTextMessage(update.getMessage().getChatId(), UNKNOWN_COMMAND));
                    break;
            }
        }
        if (update.hasCallbackQuery()) {
            execute(sendTextMessage(update.getCallbackQuery().getMessage().getChatId(), update.getCallbackQuery().getData()));
            sendMessageWithButtons(update.getCallbackQuery().getMessage().getChatId(), NEW_INFO);
        }
    }

    @SneakyThrows
    private void sendMessageWithButtons(Long chatId, String message) {
        var sendMessage = sendTextMessage(chatId, message);

        List<City> cityList = cityService.getAll();
        if (!cityList.isEmpty()) {
            List<InlineKeyboardButton> inlineKeyboardButtons = cityService.getAll().stream()
                    .map(city -> {
                        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                        inlineKeyboardButton.setText(city.getName());
                        inlineKeyboardButton.setCallbackData(city.getDescription());
                        return inlineKeyboardButton;
                    }).collect(Collectors.toList());

            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            inlineKeyboardButtons.forEach(button -> {
                keyboard.add(new ArrayList<>(Collections.singleton(button)));
            });
            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
            keyboardMarkup.setKeyboard(keyboard);
            sendMessage.setReplyMarkup(keyboardMarkup);
        } else {
            sendMessage.setText(NO_CITY);
        }
        execute(sendMessage);
    }

    @SneakyThrows
    private SendMessage sendTextMessage(Long chatId, String message) {
        return new SendMessage(chatId.toString(), message);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

}
