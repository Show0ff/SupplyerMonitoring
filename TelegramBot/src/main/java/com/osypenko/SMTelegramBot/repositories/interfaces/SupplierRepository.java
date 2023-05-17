package com.osypenko.SMTelegramBot.repositories.interfaces;

import com.osypenko.SMTelegramBot.entityies.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
