package com.shopfrontend.controller;


import com.shopfrontend.dto.AuthRequest;
import com.shopfrontend.service.ShopService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private ShopService shopService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("authRequest", new AuthRequest());
        return "login";
    }


    @PostMapping("/login")
    public String handleLogin(@ModelAttribute AuthRequest request, HttpSession session, Model model) {

        String token = shopService.login(request.getUsername(), request.getPassword());

        if (token != null) {

            session.setAttribute("MY_SESSION_TOKEN", token);
            session.setAttribute("MY_SESSION_USERNAME", request.getUsername());

            return "redirect:/";
        } else {

            model.addAttribute("error", "Sai tài khoản hoặc mật khẩu!");
            return "login";
        }
    }


    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("authRequest", new AuthRequest());
        return "register";
    }


    @PostMapping("/register")
    public String handleRegister(@ModelAttribute AuthRequest request) {
        if (shopService.register(request)) {
            return "redirect:/login?success";
        }
        return "redirect:/register?error";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Xóa sạch session
        return "redirect:/";
    }
}