package com.shopfrontend.controller;

import com.shopfrontend.dto.OrderResponse;
import com.shopfrontend.service.ShopService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class WebController {

    @Autowired
    private ShopService shopService;


    @GetMapping("/")
    public String homePage(Model model) {

        var products = shopService.getAllProducts();


        model.addAttribute("products", products);


        return "home";
    }

    @PostMapping("/buy")
    public String buyProduct(
            @RequestParam("id") Long productId,
            @RequestParam("quantity") int quantity,
            HttpSession session,
            Model model
    ) {
        String token = (String) session.getAttribute("MY_SESSION_TOKEN");
        String username = (String) session.getAttribute("MY_SESSION_USERNAME");

        if (token == null || username == null) {
            return "redirect:/login";
        }


        String orderResult = shopService.createOrder(productId, quantity, token, username);

        if (orderResult == null) {
            return "redirect:/?error=Mua that bai";
        }
        return "redirect:/?success=Mua thanh cong";
    }


    @GetMapping("/history")
    public String historyPage(HttpSession session, Model model) {
        String token = (String) session.getAttribute("MY_SESSION_TOKEN");
        String username = (String) session.getAttribute("MY_SESSION_USERNAME");

        if (token == null || username == null) {
            return "redirect:/login";
        }


        List<OrderResponse> orders = shopService.getHistory(username, token);

        model.addAttribute("orders", orders);
        return "history";
    }

}