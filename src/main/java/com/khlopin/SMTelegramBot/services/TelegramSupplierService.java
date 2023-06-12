package com.khlopin.SMTelegramBot.services;


import com.khlopin.SupplierMonitoring.entity.Supplier;
import com.khlopin.SupplierMonitoring.services.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TelegramSupplierService {
    private final SupplierService supplierService;

    //Получение информации о конкретном поставщике по его названию
    public Supplier getSupplierForName(String key) {
        Map<String, Supplier> supplierMap = new HashMap<>();
        for (Supplier supplier : supplierService.getAllSuppliers()) {
            supplierMap.put(supplier.getName(), supplier);
        }
        return supplierMap.get(key);
    }

    //Создание списка с названием всех поставщиков
    public Set<String> getSupplierSetList() {
        Set<String> suppliersName = new HashSet<>();
        for (Supplier supplier : supplierService.getAllSuppliers()) {
            suppliersName.add(supplier.getName());
        }
        return  suppliersName;
    }
}
