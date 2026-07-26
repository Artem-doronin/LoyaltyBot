package com.example.LoyaltyBot.controller;

import com.example.LoyaltyBot.dto.user.CreateUserDto;
import com.example.LoyaltyBot.dto.user.UserDto;
import com.example.LoyaltyBot.entity.User;
import com.example.LoyaltyBot.service.RoleService;
import com.example.LoyaltyBot.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    @GetMapping("delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }

    @GetMapping("/create")
    public String formCreateUser(Model model) {
        model.addAttribute("user",CreateUserDto.builder().build());
        model.addAttribute("roles", roleService.findAll());
        return "user/form";
    }


    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createUser(@ModelAttribute("user") CreateUserDto userDto) {
        userService.createUser(userDto);
        return "redirect:/users";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            request.getSession().invalidate();
        }
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "/auth/login";
    }

    @GetMapping("/error")
    public String error() {
        return "user/error";
    }

    @PostMapping("/toggle/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String toggleUser(@PathVariable Long id) {
        userService.toggleEnabled(id);
        return "redirect:/users";
    }
}


