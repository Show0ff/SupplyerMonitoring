package com.khlopin.SupplierMonitoring.services;


import com.khlopin.SupplierMonitoring.entity.Role;
import com.khlopin.SupplierMonitoring.entity.User;
import com.khlopin.SupplierMonitoring.services.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;


    private static final Logger log = LogManager.getLogger(UserService.class);

    public boolean createUser(User user) {
        Optional<User> existingUser = userRepository.findUserByLogin(user.getLogin());
        if (existingUser.isPresent()) {
            return false;
        }
        user.setCorpId(createCorpID());
        log.info("new user was saved" + user);
        userRepository.save(user);

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
        return null;
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

    public User updateUser(Long id, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setLogin(updatedUser.getLogin());
            user.setPassword(updatedUser.getPassword());
            user.setRole(updatedUser.getRole());
            log.info("User with id " + id + " was updated");
            return userRepository.save(user);
        } else {
            log.error("User with id " + id + " not found");
            return null;
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
                    .firstName("Mihail")
                    .surname("Khlopin")
                    .login("Show0ff")
                    .password("AdminPassword123456")
                    .role(Role.ADMIN).build());
            if (success) {
                log.info("admin was created");
            }
        }
    }


}
