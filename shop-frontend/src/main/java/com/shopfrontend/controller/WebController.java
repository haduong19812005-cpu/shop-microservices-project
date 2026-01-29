package com.shopfrontend.controller;

import com.shopfrontend.service.ShopService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller // Lưu ý: Dùng @Controller (trả về HTML), KHÔNG dùng @RestController
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


        if (token == null) {
            return "redirect:/login";
        }


        String orderResult = shopService.createOrder(productId, quantity, token);

        if (orderResult == null) {
            return "redirect:/?error=Mua that bai";
        }

        return "redirect:/?success=Mua thanh cong";
    }
}