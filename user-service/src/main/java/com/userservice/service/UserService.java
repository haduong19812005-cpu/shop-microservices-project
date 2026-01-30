package com.userservice.service;

import com.userservice.entity.User;
import com.userservice.repository.UserRepository;
import com.userservice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public User registerUser(User user) {

        User existingUser = repository.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("Tên đăng nhập này đã có người dùng!");
        }

        User existingEmail = repository.findByEmail(user.getEmail());
        if (existingEmail != null) {
            throw new RuntimeException("Email này đã được đăng ký rồi! Vui lòng dùng email khác.");
        }

        return repository.save(user);
    }

    public User getUserById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public String login(String username, String password) {

        User user = repository.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            return JwtUtil.generateToken(username);
        }
        return null;
    }
}
