package com.shopfrontend.service;

import com.shopfrontend.dto.AuthRequest;
import com.shopfrontend.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShopService {

    @Value("${api.gateway.url}")
    private String gatewayUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<ProductDTO> getAllProducts() {
        String url = gatewayUrl + "/api/products";


        ProductDTO[] response = restTemplate.getForObject(url, ProductDTO[].class);


        return response != null ? Arrays.asList(response) : List.of();
    }

    public String login(String username, String password) {
        String url = gatewayUrl + "/api/users/login";


        AuthRequest request = new AuthRequest();
        request.setUsername(username);
        request.setPassword(password);

        try {

            return restTemplate.postForObject(url, request, String.class);
        } catch (Exception e) {
            return null;
        }
    }


    public boolean register(AuthRequest request) {
        String url = gatewayUrl + "/api/users/register";
        try {
            restTemplate.postForObject(url, request, Object.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public String createOrder(Long productId, int quantity, String token) {
        String url = gatewayUrl + "/api/orders";


        Map<String, Object> body = new HashMap<>();
        body.put("userId", 1);
        body.put("productId", productId);
        body.put("quantity", quantity);


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {

            return restTemplate.postForObject(url, requestEntity, String.class);
        } catch (Exception e) {
            return null;
        }
    }
}