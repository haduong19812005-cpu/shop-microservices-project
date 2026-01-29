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


    public Order createOrder(CreateOrderRequest request) {

        try {
            restTemplate.getForObject(
                    "http://USER-SERVICE/users/" + request.getUserId(),
                    Object.class
            );
        } catch (Exception e) {
            throw new RuntimeException("User không tồn tại");
        }


        ProductResponse product = restTemplate.getForObject(
                "http://PRODUCT-SERVICE/products/" + request.getProductId(),
                ProductResponse.class
        );

        if (product == null) {
            throw new RuntimeException("Product không tồn tại");
        }


        restTemplate.put(
                "http://PRODUCT-SERVICE/products/reduce-quantity/"
                        + request.getProductId()
                        + "?quantity=" + request.getQuantity(),
                null
        );


        double totalPrice = product.getPrice() * request.getQuantity();


        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setProductId(product.getId());
        order.setQuantity(request.getQuantity());
        order.setTotalPrice(totalPrice);
        order.setCreatedAt(LocalDateTime.now());


        return orderRepository.save(order);
    }


    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
