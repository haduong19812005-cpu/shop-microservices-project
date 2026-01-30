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
    public String createOrder(Long productId, int quantity, String token, String username) {
        String url = gatewayUrl + "/api/orders";

        Map<String, Object> body = new HashMap<>();
        body.put("userId", 1);
        body.put("productId", productId);
        body.put("quantity", quantity);

        body.put("username", username);
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

    public List<com.shopfrontend.dto.OrderResponse> getHistory(String username, String token) {
        String url = gatewayUrl + "/api/orders/my-orders/" + username;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {

            org.springframework.http.ResponseEntity<com.shopfrontend.dto.OrderResponse[]> response =
                    restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, com.shopfrontend.dto.OrderResponse[].class);

            return response.getBody() != null ? Arrays.asList(response.getBody()) : List.of();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}