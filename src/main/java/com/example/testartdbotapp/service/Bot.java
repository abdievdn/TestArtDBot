package com.example.testartdbotapp.service;

import com.example.testartdbotapp.config.BotConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class Bot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    @Override
    public void onRegister() {
        List<BotCommand> botCommandList = new ArrayList<>();
        botCommandList.add(new BotCommand("/start", BotInterface.BOT_COMMAND_START));
        botCommandList.add(new BotCommand("/help", BotInterface.BOT_COMMAND_HELP));
        botCommandList.add(new BotCommand("/info", BotInterface.BOT_COMMAND_INFO));
        botCommandList.add(new BotCommand("/input", BotInterface.BOT_COMMAND_INPUT));
        botCommandList.add(new BotCommand("/show", BotInterface.BOT_COMMAND_SHOW));
        botCommandList.add(new BotCommand("/edit", BotInterface.BOT_COMMAND_EDIT));
        try {
            this.execute(new SetMyCommands(botCommandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String inputMessage = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getChat().getFirstName();
            String messageText = "Здравствуйте, " + userName + ". Рады Вас видеть!";
            switch (inputMessage) {
                case "/start":
                    sendMessageBot(chatId, messageText);
                    break;
                case "/help":
                    sendMessageBot(chatId, BotInterface.BOT_TEXT_HELP);
                    break;
                default:
                    sendMessageBot(chatId, "Неверна введена команда.");
            }

        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    private void sendMessageBot(Long chatId, String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
