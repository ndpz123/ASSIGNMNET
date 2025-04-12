package com.jetbrains.assign.controller;

import com.jetbrains.assign.service.UserService;
import com.jetbrains.assign.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;

    // Hiển thị danh sách người dùng và một đối tượng người dùng mới
    @GetMapping
    public String index(Model model) {
        model.addAttribute("usersList", userService.getAll()); // Danh sách người dùng
        model.addAttribute("user", new Users()); // Đối tượng người dùng mới để tạo
        return "product";
    }

    // Lưu người dùng mới
    @PostMapping("/save")
    public String save(@ModelAttribute Users user) {
        userService.save(user); // Lưu thông tin người dùng
        return "redirect:/users"; // Sau khi lưu xong, điều hướng đến /users
    }

    // Chỉnh sửa người dùng
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        Users user = userService.getById(id);
        if (user != null) {
            model.addAttribute("user", user); // Hiển thị thông tin người dùng cần chỉnh sửa
            model.addAttribute("usersList", userService.getAll()); // Hiển thị danh sách tất cả người dùng
            return "product";
        } else {
            return "redirect:/users"; // Nếu không tìm thấy người dùng, điều hướng về trang danh sách
        }
    }

    // Cập nhật thông tin người dùng
    @PostMapping("/update")
    public String update(@ModelAttribute Users user) {
        userService.update(user.getId(), user); // Cập nhật thông tin người dùng
        return "redirect:/users"; // Sau khi cập nhật xong, điều hướng đến /users
    }

    // Xóa người dùng
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        userService.delete(id); // Xóa người dùng theo id
        return "redirect:/users"; // Sau khi xóa xong, điều hướng đến /users
    }
}
