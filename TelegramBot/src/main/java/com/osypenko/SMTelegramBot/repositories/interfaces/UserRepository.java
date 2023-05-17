package com.osypenko.SMTelegramBot.repositories.interfaces;

import com.osypenko.SMTelegramBot.entityies.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}