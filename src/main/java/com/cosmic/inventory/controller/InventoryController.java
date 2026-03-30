package com.cosmic.inventory.controller;

import com.cosmic.inventory.model.Product;
import com.cosmic.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("file") MultipartFile file) {
        
        try {
            Product savedProduct = inventoryService.addProduct(name, description, price, file);
            return ResponseEntity.ok(savedProduct);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<java.util.List<Product>> getAllProducts() {
        return ResponseEntity.ok(inventoryService.getAllProducts());
    }
}