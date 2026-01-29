package com.productservice.service;

import com.productservice.dto.ProductRequest;
import com.productservice.dto.ProductResponse;
import com.productservice.entity.Product;
import com.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());

        Product savedProduct = repository.save(product);
        return new ProductResponse(savedProduct.getId(), savedProduct.getName(), savedProduct.getPrice(), savedProduct.getStockQuantity());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = repository.findAll();
        return products.stream()
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getStockQuantity()))
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long id) {
        Product product = repository.findById(id).orElse(null);
        if (product != null) {
            return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getStockQuantity());
        }
        return null;
    }

    public ProductResponse reduceStock(Long id, int quantity) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Sản phẩm đã hết hàng");
        }
        product.setStockQuantity(product.getStockQuantity() - quantity);
        Product savedProduct = repository.save(product);
        return new ProductResponse(savedProduct.getId(), savedProduct.getName(), savedProduct.getPrice(), savedProduct.getStockQuantity());
    }
}