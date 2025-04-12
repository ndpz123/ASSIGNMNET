package com.jetbrains.assign.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Điều hướng đến trang chủ (index.html)
    @GetMapping("/")
    public String home() {
        return "index"; // trả về file templates/index.html
    }

    // Điều hướng đến trang quản lý sản phẩm
    @GetMapping("/manage/products")
    public String manageProducts() {
        return "redirect:/products"; // sử dụng lại controller product
    }

    // Điều hướng đến trang quản lý người dùng
    @GetMapping("/manage/users")
    public String manageUsers() {
        return "redirect:/users"; // sử dụng lại controller users
    }


    @GetMapping("/login")
    public String login() {
        return "login"; // file login.html nằm trong templates
    }

    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

    @GetMapping("/cart")
    public String cart(){
        return "cart";
    }
    @GetMapping("/logout")
    public String logout(){
        return "login";
    }
    @GetMapping("/register")
    public String register(){
        return "register";
    }
}
