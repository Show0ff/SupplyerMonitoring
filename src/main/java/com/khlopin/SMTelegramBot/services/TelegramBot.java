package com.khlopin.SMTelegramBot.services;


import com.khlopin.SMTelegramBot.config.BotConfig;
import com.khlopin.SupplierMonitoring.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.khlopin.SMTelegramBot.constant.Constant.*;
import static com.khlopin.SMTelegramBot.constant.LogsConstant.*;


@Slf4j
@Controller
public class TelegramBot extends TelegramLongPollingBot {
    private final TelegramUserService userService;
    private final ViewInformation viewInformation;
    private final BotConfig config;

    public TelegramBot(BotConfig config, TelegramUserService userService, ViewInformation viewInformation) {
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand(START, LOG_IN));
        listOfCommands.add(new BotCommand(EXIT, LOG_OUT));
        listOfCommands.add(new BotCommand(INFO, HELP));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(ERROR_SETTING_BOT_COMMAND_LIST + e.getMessage());
        }
        this.userService = userService;
        this.viewInformation = viewInformation;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    //Вывод сообщений Пользователю
    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        buttonMenu(message);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(ERROR_METHOD_SEND_MESSAGE + e.getMessage());
        }
    }

    //Отображение главных кнопок
    private void buttonMenu(SendMessage message) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(SUPPLIERS);
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add(TASKS);
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);
    }

    //Обработка сообщений от Пользователя
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case START -> sendMessage(chatId, ENTER_YOUR_COMPANY_CODE_TO_ACCESS_THE_SYSTEM);
                case EXIT -> {
                    userService.userLogOut(chatId);
                    sendMessage(chatId, YOU_ARE_LOGGED_OUT_OF_THE_SYSTEM);
                }
                case INFO-> sendMessage(chatId, YOU_CAN_ONLY_LOG_IN_USING_YOUR_CORPORATE_NUMBER);
                case SUPPLIERS -> {
                    SendMessage message = viewInformation.userAccessForInformation(chatId, VIEW_SUPPLIERS);
                    sendMessageInfoEntity(chatId, message);
                }
                case TASKS -> {
                    SendMessage message = viewInformation.userAccessForInformation(chatId, TASK_VIEW);
                    sendMessageInfoEntity(chatId, message);
                }
                default -> searchCorporateId(chatId, messageText);
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            String text = viewInformation.viewAllInformForEntity(callbackData);

            EditMessageText messageText = new EditMessageText();
            messageText.setChatId(String.valueOf(chatId));
            messageText.setText(text);
            messageText.setMessageId((int) messageId);

            try {
                execute(messageText);
            } catch (TelegramApiException e) {
                log.error(ERROR_METHOD_ON_UPDATE_RECEIVED + e.getMessage());
            }
        }
    }

    //Отправка сообщения полной информацией про запрашиваемую сущность
    private void sendMessageInfoEntity(long chatId, SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            sendMessage(chatId, DO_NOT_ACCESS);
            log.error(ERROR_METHOD_VIEW_LIST_ENTITY);
        }
    }

    //Поиска пользователя по корп номеру в базе данных для доступа к данным
    private void searchCorporateId(long chatId, String messageText) {
        HashMap<Integer, User> userMap = userService.getUserForCorpId();

        try {
            int numCorpId = Integer.parseInt(messageText);
            if (userMap.containsKey(numCorpId)) {

                User verifiedUser = userMap.get(numCorpId);
                verifiedUser.setAccessTelegramBot(chatId);
                userService.updateUser(verifiedUser);

//                sendMessage(chatId, WELCOME + verifiedUser.getFirstName());
                sendMessage(chatId, WELCOME + verifiedUser.getFirstName());
                log.info(USER_LOGGED_IN);
            } else {
                sendMessage(chatId, NO_EMPLOYEE_WITH_THIS_NUMBER_WAS_FOUND);
                log.info(NO_USER_IN_DB);
            }
        } catch (NumberFormatException e) {
            sendMessage(chatId, THE_NUMBER_WAS_ENTERED_INCORRECTLY);
            log.error(INCORRECTLY_CORP_NUM);
        }
    }
}
