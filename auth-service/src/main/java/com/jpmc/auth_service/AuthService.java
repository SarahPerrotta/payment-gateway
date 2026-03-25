package com.jpmc.auth_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String register(String username, String password) {
        if (userRepository.findByUsername(username) != null) {
            return "Username already exists";
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return "User registered successfully";
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "User not found";
        }
        if (passwordEncoder.matches(password, user.getPassword())) {
            return "Login successful";
        }
        return "Invalid password";
    }
}
