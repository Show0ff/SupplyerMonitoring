package com.osypenko.SMTelegramBot.services;

import com.osypenko.SMTelegramBot.entity.Supplier;
import com.osypenko.SMTelegramBot.entity.Task;
import com.osypenko.SMTelegramBot.repositories.SupplierService;
import com.osypenko.SMTelegramBot.repositories.TaskService;
import com.osypenko.SMTelegramBot.repositories.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.osypenko.SMTelegramBot.constant.Constant.*;
import static com.osypenko.SMTelegramBot.constant.ConstantMessage.*;

@Slf4j
@Controller
public class ViewInformation {
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    //Проверка Пользователя на разрешение к информации
    public SendMessage userAccessForInformation(long chatId, String nameClass) {
        Set<Long> allUsers = userService.getSetListUsersCorpId();

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
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(allTask.getExtraInfo());
            button.setCallbackData(allTask.getExtraInfo());
            rowInLine.add(button);
        }
        rowsInLine.add(rowInLine);
    }

    //Вывод сообщения полной информации о Поставщике или Задании
    public String viewAllInformForEntity(String callbackData) {
        String text = TEXT;

        Set<String> supplierSetList = supplierService.getSupplierSetList();
        if (supplierSetList.contains(callbackData)) {
            Supplier supplier = supplierService.getSupplierForName(callbackData);
            text = supplier.getName() +
                    PRICE + supplier.getPrice() +
                    RATING + supplier.getRating() +
                    SPEED + supplier.getDeliverySpeed() +
                    COMMENT + supplier.getComment();
        }

        Set<String> taskSetList = taskService.getTaskSetList();
        if (taskSetList.contains(callbackData)) {
            Task task = taskService.getTaskForExtraInfo(callbackData);
            text = TASK + task.getExtraInfo() +
                    DESCRIPTION + task.getText() +
                    STATUS + task.getTaskStatus();
        }

        return text;
    }
}
