package com.example.LoyaltyBot.controller;

import com.example.LoyaltyBot.dto.user.ChangePasswordRequest;
import com.example.LoyaltyBot.dto.user.CreateUserDto;
import com.example.LoyaltyBot.dto.user.TemporaryPasswordResponse;
import com.example.LoyaltyBot.service.RoleService;
import com.example.LoyaltyBot.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @PreAuthorize("hasRole('ADMIN')")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    @GetMapping("delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String formCreateUser(Model model) {
        model.addAttribute("user", CreateUserDto.builder().build());
        model.addAttribute("roles", roleService.findAll());
        return "user/form";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createUser(@ModelAttribute("user") CreateUserDto userDto,
                             RedirectAttributes redirectAttributes) {
        TemporaryPasswordResponse dto = userService.createUser(userDto);
        redirectAttributes.addFlashAttribute("temporaryPassword", dto.temporaryPassword());
        redirectAttributes.addFlashAttribute("login", dto.login());
        return "redirect:/users/password-create-response";
    }

    @PostMapping("/reset_password/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String resetPassword(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        TemporaryPasswordResponse dto = userService.resetPassword(id);
        redirectAttributes.addFlashAttribute("temporaryPassword", dto.temporaryPassword());
        redirectAttributes.addFlashAttribute("login", dto.login());
        return "redirect:/users/password-response";
    }

    @GetMapping("/password-response")
    public String passwordResponse() {
        return "user/password-reset-response";
    }



    @GetMapping("/password-create-response")
    public String passwordCreateResponse() {
        return "user/create-response";
    }


    @PostMapping("/toggle-enabled/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String toggleUser(@PathVariable Long id) {
        userService.toggleEnabled(id);
        return "redirect:/users";
    }

    @PostMapping("/change_password")
    @PreAuthorize("hasRole('USER')")
    public String changePassword(@ModelAttribute("user") ChangePasswordRequest dto,
                                 RedirectAttributes redirectAttributes) {
        try {
            userService.changePassword(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Пароль успешно изменен!");
            return "redirect:/clients";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users/change_password";
        }
    }

    @GetMapping("/change_password")
    @PreAuthorize("hasRole('USER')")
    public String changePasswordForm(Model model) {
        model.addAttribute("user", ChangePasswordRequest.builder().build());
        return "user/change-password";
    }
}


