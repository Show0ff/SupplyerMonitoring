package com.khlopin.SMTelegramBot.services;


import com.khlopin.SupplierMonitoring.entity.Supplier;
import com.khlopin.SupplierMonitoring.entity.Task;
import com.khlopin.SupplierMonitoring.services.SupplierService;
import com.khlopin.SupplierMonitoring.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.khlopin.SMTelegramBot.constant.Constant.LIST_SUPPLIERS;
import static com.khlopin.SMTelegramBot.constant.Constant.LIST_TASKS;
import static com.khlopin.SMTelegramBot.constant.ConstantMessage.*;


@Controller
@RequiredArgsConstructor
public class ViewInformation {
    private final TelegramSupplierService telegramSupplierService;
    private final TelegramUserService telegramUserService;
    private final TelegramTaskService telegramTaskService;

    private final SupplierService supplierService;
    private final TaskService taskService;


    //Проверка Пользователя на разрешение к информации
    public SendMessage userAccessForInformation(long chatId, String nameClass) {
        Set<Long> allUsers = telegramUserService.getSetListUsersCorpId();

        for (Long allUser : allUsers) {
            if (allUser.equals(chatId)) {
                if (nameClass.equals(Supplier.class.getSimpleName())) {
                    return viewListEntity(chatId, Supplier.class);
                } else if (nameClass.equals(Task.class.getSimpleName())) {
                    return viewListEntity(chatId, Task.class);
                }
            }
        }
        return null;
    }

    //Отображение списка кнопок с Поставщиками или Заданиями
    private SendMessage viewListEntity(long chatId, Object object) {
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));

        if (object.equals(Supplier.class)) {
            viewListSupplier(message, rowInLine, rowsInLine);
        } else if (object.equals(Task.class)) {
            viewListTask(message, rowInLine, rowsInLine);
        }

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        return message;
    }

    //Вывод списка Поставщиков
    private void viewListSupplier(SendMessage message, List<InlineKeyboardButton> rowInLine, List<List<InlineKeyboardButton>> rowsInLine) {
        message.setText(LIST_SUPPLIERS);

        List<Supplier> allSuppliers = supplierService.getAllSuppliers();

        for (Supplier allSupplier : allSuppliers) {
            if (rowInLine.size() >= 4) {
                break;
            }
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(allSupplier.getName());
            button.setCallbackData(allSupplier.getName());
            rowInLine.add(button);
        }
        rowsInLine.add(rowInLine);
    }


    //Вывод списка Заданий
    private void viewListTask(SendMessage message, List<InlineKeyboardButton> rowInLine, List<List<InlineKeyboardButton>> rowsInLine) {
        message.setText(LIST_TASKS);

        List<Task> allTasks = taskService.getAllTasks();

        for (Task allTask : allTasks) {
            if (rowInLine.size() >= 4) {
                break;
            }
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(allTask.getText());
            button.setCallbackData(allTask.getText());
            rowInLine.add(button);
        }
        rowsInLine.add(rowInLine);
    }


    //Вывод сообщения полной информации о Поставщике или Задании
    public String viewAllInformForEntity(String callbackData) {
        String text = TEXT;

        Set<String> supplierSetList = telegramSupplierService.getSupplierSetList();
        if (supplierSetList.contains(callbackData)) {
            Supplier supplier = telegramSupplierService.getSupplierForName(callbackData);
            text = supplier.getName() +
                    PRICE + supplier.getPrice() +
                    RATING + supplier.getRating() +
                    SPEED + supplier.getDeliverySpeed() +
                    COMMENT + supplier.getComment();
        }

        Set<String> taskSetList = telegramTaskService.getTaskSetList();
        if (taskSetList.contains(callbackData)) {
            Task task = telegramTaskService.getTaskForExtraInfo(callbackData);
            text = TASK + task.getText() +
                    DESCRIPTION + task.getExtraInfo() +
                    STATUS + task.getTaskStatus();
        }

        return text;
    }
}
