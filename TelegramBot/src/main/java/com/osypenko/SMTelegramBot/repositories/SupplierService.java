package com.osypenko.SMTelegramBot.repositories;

import com.osypenko.SMTelegramBot.entityies.Supplier;
import com.osypenko.SMTelegramBot.repositories.interfaces.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;

    //Создание списка поставщиков
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    //Получение информации о конкретном поставщике по его названию
    public Supplier getSupplierForName(String key) {
        Map<String, Supplier> supplierMap = new HashMap<>();
        for (Supplier supplier : getAllSuppliers()) {
            supplierMap.put(supplier.getName(), supplier);
        }
        return supplierMap.get(key);
    }

    //Создание списка с названием всех поставщиков
    public Set<String> getSupplierSetList() {
        Set<String> suppliersName = new HashSet<>();
        for (Supplier supplier : getAllSuppliers()) {
            suppliersName.add(supplier.getName());
        }
        return  suppliersName;
    }
}
