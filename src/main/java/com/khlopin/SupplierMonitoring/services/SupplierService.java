package com.khlopin.SupplierMonitoring.services;

import com.khlopin.SupplierMonitoring.entity.Supplier;
import com.khlopin.SupplierMonitoring.services.repositories.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private static final Logger log = LogManager.getLogger(SupplierService.class);

    public List<Supplier> getAllSuppliers() {
        return (List<Supplier>) supplierRepository.findAll();
    }

    public void createSupplier(Supplier supplier) {
        supplierRepository.save(supplier);
        log.info(supplier + " поставщик был создан");
    }

    public void deleteSupplier(Long id) {
        Optional<Supplier> supplier = supplierRepository.findById(id);
        if (supplier.isPresent()) {
        supplierRepository.deleteById(id); }
        log.info(supplier + " поставщик был удалён");

    }

}
