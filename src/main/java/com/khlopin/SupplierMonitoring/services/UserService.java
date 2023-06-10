package com.khlopin.SupplierMonitoring.services;


import com.khlopin.SupplierMonitoring.entity.Role;
import com.khlopin.SupplierMonitoring.entity.User;
import com.khlopin.SupplierMonitoring.services.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;

    private static final String uploadDirectory = System.getProperty("user.dir") + "/uploads";

    private static final Logger log = LogManager.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean createUser(User user) {
        if (userRepository.findUserByLogin(user.getLogin()).isPresent()) {
            return false;
        }

        user.setCorpId(createCorpID());
        userRepository.save(user);
        log.info("New user was saved: " + user);

        return true;
    }


    public Optional<User> authUser(String login, String password) {
        Optional<User> existingUser = userRepository.findUserByLogin(login);
        if (existingUser.isPresent()) {
            if (password.equals(existingUser.get().getPassword())) {
                log.info("user has been auth " + existingUser.get());
                return existingUser;
            }
        }
        return Optional.empty();
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            log.error(user + " not found");
            return null;
        }
    }

    public void updateUser(Long id, User updatedUser) {
        Optional<User> existingUserOptional = userRepository.findById(id);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            if (updatedUser.getLogin() != null) {
                existingUser.setLogin(updatedUser.getLogin());
            }
            if (updatedUser.getFirstName() != null) {
                existingUser.setFirstName(updatedUser.getFirstName());
            }
            if (updatedUser.getSurname() != null) {
                existingUser.setSurname(updatedUser.getSurname());
            }
            if (updatedUser.getPatronymic() != null) {
                existingUser.setPatronymic(updatedUser.getPatronymic());
            }
            if (updatedUser.getPassword() != null) {
                existingUser.setPassword(updatedUser.getPassword());
            }
            if (updatedUser.getRole() != null) {
                existingUser.setRole(updatedUser.getRole());
            }

            log.info("User with id " + id + " was updated");
            userRepository.save(existingUser);
        } else {
            log.error("User with id " + id + " not found");
        }
    }


    private Integer createCorpID() {
        int randomNumber = (int) (Math.random() * 1000000);
        String corpIDString = String.format("%06d", randomNumber);
        return Integer.parseInt(corpIDString);
    }

    public void createAdmin() {
        if (userRepository.findUserByLogin("Show0ff").isEmpty()) {
            boolean success = createUser(User.builder()
                    .firstName("Михаил")
                    .surname("Хлопин")
                    .patronymic("Валерьевич")
                    .login("Show0ff")
                    .password("AdminPassword123456")
                    .role(Role.ADMIN).build());
            if (success) {
                log.info("admin was created");
            }
        }
    }


    public void uploadAvatar(MultipartFile file, User user) {
        try {
            Path dirPath = Paths.get(uploadDirectory);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            if (user.getAvatarPath() != null) {
                Path previousFilePath = dirPath.resolve(user.getAvatarPath().substring(9));
                Files.deleteIfExists(previousFilePath);
            }

            Path filePath = dirPath.resolve(Objects.requireNonNull(file.getOriginalFilename()));
            Files.write(filePath, file.getBytes());

            user.setAvatarPath("uploads/" + file.getOriginalFilename());
            userRepository.save(user);
        } catch (IOException e) {
            log.error("Error during saving file", e);
        }
    }

}
