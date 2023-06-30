package com.example.testartdbotapp.controller;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class BotInterface {

    public final static String BOT_COMMAND_START = "Запуск бота";
    public final static String BOT_COMMAND_HELP = "Показать список команд";
    public final static String BOT_COMMAND_INFO = "Информация о текущем пользователе";
    public final static String BOT_COMMAND_INPUT = "Ввод данных";
    public final static String BOT_COMMAND_SHOW = "Показать данные";
    public final static String BOT_COMMAND_EDIT = "Редактирование данных";

    public final static String BOT_TEXT_HELP = "Для взаимодействия с ботом используйте следующие команды: \n\n" +
            "/start - " + BOT_COMMAND_START + "\n" +
            "/help - " + BOT_COMMAND_HELP + "\n" +
            "/info - " + BOT_COMMAND_INFO + "\n" +
            "/input - " + BOT_COMMAND_INPUT + "\n" +
            "/show - " + BOT_COMMAND_SHOW + "\n" +
            "/edit - " + BOT_COMMAND_EDIT;

    public final static String RECEIPTS_INPUT_BUTTON = "RECEIPTS_INPUT_BUTTON";
    public final static String EXPENSES_INPUT_BUTTON = "EXPENSES_INPUT_BUTTON";
    public final static String SAFETY_STOCK_INPUT_BUTTON = "SAFETY_STOCK_INPUT_BUTTON";
    public final static String RECEIPTS_SHOW_BUTTON = "RECEIPTS_SHOW_BUTTON";
    public final static String EXPENSES_SHOW_BUTTON = "EXPENSES_SHOW_BUTTON";
    public final static String SAFETY_STOCK_SHOW_BUTTON = "SAFETY_STOCK_SHOW_BUTTON";
    public static void botKeyboard(SendMessage message) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Погода");
        row.add("Курсы");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("Приход");
        row.add("Расход");
        row.add("НЗ");
        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);
    }

    public static void botButtons(SendMessage message,
                                  String receiptsStateButton, String expensesStateButton, String safetyStockStateButton ) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        InlineKeyboardButton receiptsButton = InlineKeyboardButton.builder()
                .text("Приход")
                .callbackData(receiptsStateButton)
                .build();
        InlineKeyboardButton expensesButton = InlineKeyboardButton.builder()
                .text("Расход")
                .callbackData(expensesStateButton)
                .build();
        InlineKeyboardButton SafetyStockButton = InlineKeyboardButton.builder()
                .text("Расход")
                .callbackData(safetyStockStateButton)
                .build();
        rowInLine.add(receiptsButton);
        rowInLine.add(expensesButton);
        rowsInLine.add(rowInLine);
        keyboardMarkup.setKeyboard(rowsInLine);
        message.setReplyMarkup(keyboardMarkup);
    }
}
