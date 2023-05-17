package com.osypenko.SMTelegramBot.repositories.interfaces;

import com.osypenko.SMTelegramBot.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
