package com.productservice.controller;

import com.productservice.dto.ProductRequest;
import com.productservice.dto.ProductResponse;
import com.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping
    public ProductResponse createProduct(@RequestBody ProductRequest productRequest) {
        return service.createProduct(productRequest);
    }
    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return service.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        return service.getProductById(id);
    }

    @PutMapping("/reduce-quantity/{id}")
    public ProductResponse reduceQuantity(
            @PathVariable Long id,
            @RequestParam int quantity
    ) {
        return service.reduceStock(id, quantity);
    }
}