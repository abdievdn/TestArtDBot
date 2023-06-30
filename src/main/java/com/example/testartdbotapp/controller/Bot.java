package com.example.testartdbotapp.controller;

import com.example.testartdbotapp.config.BotConfig;
import com.example.testartdbotapp.service.ReceiptService;
import com.example.testartdbotapp.service.UserService;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static com.example.testartdbotapp.controller.BotInterface.*;
import static com.example.testartdbotapp.controller.BotInterface.EXPENSES_SHOW_BUTTON;

@Slf4j
@RequiredArgsConstructor
@Component
public class Bot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final UserService userService;
    private final ReceiptService receiptService;
    private String callBackData;

    @Override
    public void onRegister() {
        List<BotCommand> botCommandList = new ArrayList<>();
        botCommandList.add(new BotCommand("/start", BOT_COMMAND_START));
        botCommandList.add(new BotCommand("/help", BOT_COMMAND_HELP));
        botCommandList.add(new BotCommand("/info", BOT_COMMAND_INFO));
        botCommandList.add(new BotCommand("/input", BOT_COMMAND_INPUT));
        botCommandList.add(new BotCommand("/show", BOT_COMMAND_SHOW));
        botCommandList.add(new BotCommand("/edit", BOT_COMMAND_EDIT));
        try {
            this.execute(new SetMyCommands(botCommandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            if (callBackData != null) {
                String text = update.getMessage().getText();
                switch (callBackData) {
                    case RECEIPTS_INPUT_BUTTON:
                        try {
                            Long value = Long.parseLong(text);
                            receiptService.saveReceipts(value);
                            sendBotMessage(chatId, "Значение успешно сохранено");
                        } catch (Exception e) {
                            sendBotMessage(chatId, "Неверно введено значение!");
                        }
                        break;
                    case (RECEIPTS_SHOW_BUTTON):
                        String[] dateValues = text.split(" ");
                        try {
                            Integer year = Integer.parseInt(dateValues[0]);
                            Integer month = Integer.parseInt(dateValues[1]);
                            Long receiptValue = userService.showReceiptValue(chatId, year, month);
                            sendBotMessage(chatId, "Приход за " + month + " месяц " +
                                    year + " год составляет " + receiptValue);
                        } catch (Exception e) {
                            sendBotMessage(chatId, "Неверно введены значения!");
                        }
                        break;
                }
                callBackData = null;
            } else {
                String inputMessage = update.getMessage().getText();
                Chat chat = update.getMessage().getChat();
                String userName = update.getMessage().getChat().getFirstName();
                switch (inputMessage) {
                    case "/start":
                        userService.checkUser(chatId);
                        String messageText = EmojiParser.parseToUnicode("Здравствуйте, " + userName + ". Рады Вас видеть! " + ":blush:");
                        sendBotMessage(chatId, messageText);
                        break;
                    case "/help":
                        sendBotMessage(chatId, BOT_TEXT_HELP);
                        break;
                    case "/info":
                        sendBotMessage(chatId, userService.getUserInfo(chatId, chat));
                        break;
                    case "/input":
                        SendMessage messageInputText = new SendMessage();
                        messageInputText.setChatId(chatId);
                        messageInputText.setText("Выберите позицию для ввода: ");
                        botButtons(messageInputText, RECEIPTS_INPUT_BUTTON, EXPENSES_INPUT_BUTTON, SAFETY_STOCK_INPUT_BUTTON);
                        executeBotMessage(messageInputText);
                        break;
                    case "/show":
                        SendMessage messageShowText = new SendMessage();
                        messageShowText.setChatId(chatId);
                        messageShowText.setText("Выберите позицию для показа: ");
                        botButtons(messageShowText, RECEIPTS_SHOW_BUTTON, EXPENSES_SHOW_BUTTON, SAFETY_STOCK_SHOW_BUTTON);
                        executeBotMessage(messageShowText);
                        break;
                    default:
                        sendBotMessage(chatId, "Неверна введена команда.");
                }
            }
        } else if (update.hasCallbackQuery()) {
            callBackData = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            String messageText;
            switch (callBackData) {
                case RECEIPTS_INPUT_BUTTON:
                    messageText = "Введите значение для прихода";
                    sendBotMessage(chatId, messageText);
                    break;
                case RECEIPTS_SHOW_BUTTON:
                    messageText = "Введите значения года и месяца через пробел";
                    sendBotMessage(chatId, messageText);

                    break;
                case EXPENSES_INPUT_BUTTON:
                    messageText = "Введите значение для расхода";
                    sendBotMessage(chatId, messageText);
                    break;
            }
        }

    }

    private void executeBotMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveReceipts() {

    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    private void sendBotMessage(Long chatId, String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText);
        //botKeyboard(message);
        executeBotMessage(message);
    }
}
