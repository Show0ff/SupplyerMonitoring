package com.khlopin.SupplierMonitoring.controllers;

import com.khlopin.SupplierMonitoring.entity.Supplier;
import com.khlopin.SupplierMonitoring.services.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    @GetMapping("/manage-suppliers")
    public String showManageSuppliers(Model model) {
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("newSupplier", new Supplier());
        return "manage-suppliers";
    }

    @PostMapping("/manage-suppliers")
    public String createSupplier(Supplier newSupplier) {
        supplierService.createSupplier(newSupplier);
        return "redirect:/manage-suppliers";
    }
}
