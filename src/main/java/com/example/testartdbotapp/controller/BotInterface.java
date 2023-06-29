package com.example.testartdbotapp.controller;

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
}
