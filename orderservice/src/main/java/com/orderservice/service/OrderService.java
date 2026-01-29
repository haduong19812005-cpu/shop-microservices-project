package com.orderservice.service;

import com.orderservice.dto.CreateOrderRequest;
import com.orderservice.dto.ProductResponse;
import com.orderservice.entity.Order;
import com.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    public OrderService(OrderRepository orderRepository, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
    }

    // =======================
    // CHỨC NĂNG 1: ĐẶT HÀNG
    // =======================
    public Order createOrder(CreateOrderRequest request) {

        // 1️⃣ KIỂM TRA USER CÓ TỒN TẠI KHÔNG
        try {
            restTemplate.getForObject(
                    "http://USER-SERVICE/users/" + request.getUserId(),
                    Object.class
            );
        } catch (Exception e) {
            throw new RuntimeException("User không tồn tại");
        }

        // 2️⃣ LẤY THÔNG TIN PRODUCT
        ProductResponse product = restTemplate.getForObject(
                "http://PRODUCT-SERVICE/products/" + request.getProductId(),
                ProductResponse.class
        );

        if (product == null) {
            throw new RuntimeException("Product không tồn tại");
        }

        // 3️⃣ TRỪ TỒN KHO PRODUCT
        restTemplate.put(
                "http://PRODUCT-SERVICE/products/reduce-quantity/"
                        + request.getProductId()
                        + "?quantity=" + request.getQuantity(),
                null
        );

        // 4️⃣ TÍNH TỔNG TIỀN
        double totalPrice = product.getPrice() * request.getQuantity();

        // 5️⃣ TẠO ORDER
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setProductId(product.getId());
        order.setQuantity(request.getQuantity());
        order.setTotalPrice(totalPrice);
        order.setCreatedAt(LocalDateTime.now());

        // 6️⃣ LƯU DB
        return orderRepository.save(order);
    }

    // ===================================
    // CHỨC NĂNG 2: XEM LỊCH SỬ ĐƠN HÀNG
    // ===================================
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
