package com.khlopin.SupplierMonitoring.services.repositories;

import com.khlopin.SupplierMonitoring.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long> {
    Optional<User> findUserByLogin(String login);
}
