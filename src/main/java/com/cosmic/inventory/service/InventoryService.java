package com.cosmic.inventory.service;

import com.cosmic.inventory.model.Product;
import com.cosmic.inventory.repo.ProductRepository;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class InventoryService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Storage storage; // Auto-configured by GCP Starter

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    public Product addProduct(String name, String description, Double price, MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        storage.create(blobInfo, file.getBytes());

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setImageUrl("https://storage.googleapis.com/" + bucketName + "/" + fileName);
        
        return productRepository.save(product);
    }
    public java.util.List<Product> getAllProducts() {
    return productRepository.findAll();
    }
}