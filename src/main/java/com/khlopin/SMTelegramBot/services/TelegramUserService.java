package com.khlopin.SMTelegramBot.services;


import com.khlopin.SupplierMonitoring.entity.User;
import com.khlopin.SupplierMonitoring.services.UserService;
import com.khlopin.SupplierMonitoring.services.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TelegramUserService {

    private final UserService userService;
    private final UserRepository userRepository;


    //Создание списка всех Пользователей
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    //Создание map-ы, ключ ето корп номер, значение пользователь под этим номером
    public HashMap<Integer, User> getUserForCorpId() {
        HashMap<Integer, User> userMap = new HashMap<>();
        for (User user : getAllUsers()) {
            userMap.put(user.getCorpId(), user);
        }
        return userMap;
    }

    //Создание set-а со всеми корпоративными номерами
    public Set<Long> getSetListUsersCorpId() {
        Set<Long> userChatId = new HashSet<>();
        for (User user : getAllUsers()) {
            userChatId.add(user.getAccessTelegramBot());
        }
        return userChatId;
    }

    //Удаление chatID телеграмма в базе что-бы ограничить доступ
    public void userLogOut(long chatId) {
        for (User user : getAllUsers()) {
            if (user.getAccessTelegramBot().equals(chatId)) {
                user.setAccessTelegramBot(0L);
                updateUser(user);
            }
        }
    }

    //Изменения Пользователя (добавление chatId телеграмма в базу для доступа к данным
    public void updateUser(User updateUser) {
        userRepository.save(updateUser);
    }
}
