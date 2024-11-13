package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class MainService {
    String username = "admin";
    String password = "admin";
    public String login(String username, String password) {
        if (username.equals("admin") && password.equals("admin")) {
            return "Login Successful";
        } else {
            return "Login Failed";
        }
    }
}
