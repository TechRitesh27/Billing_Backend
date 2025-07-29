package com.URsolutions.Billing_System.controller;

import com.URsolutions.Billing_System.model.Product;
import com.URsolutions.Billing_System.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.annotation.PostConstruct;


@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "billing-software-ecigauf9q-riteshs-projects-eb2687fd.vercel.app") // Allow frontend access
public class ProductController {

    @PostConstruct
    public void init() {
        System.out.println("✅ ProductController loaded");
    }

    @Autowired
    private ProductRepository productRepository;

    // Get all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Add new product
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // Update product
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setName(updatedProduct.getName());
        product.setPrice(updatedProduct.getPrice());
        product.setStock(updatedProduct.getStock());
        return productRepository.save(product);
    }

    // Delete product
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}