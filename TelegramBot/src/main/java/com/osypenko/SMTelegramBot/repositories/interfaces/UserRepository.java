package com.osypenko.SMTelegramBot.repositories.interfaces;

import com.osypenko.SMTelegramBot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}