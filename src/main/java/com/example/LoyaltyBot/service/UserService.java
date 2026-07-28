package com.example.LoyaltyBot.service;

import com.example.LoyaltyBot.dto.user.CreateUserDto;
import com.example.LoyaltyBot.dto.user.TemporaryPasswordResponse;
import com.example.LoyaltyBot.dto.user.UserDto;
import com.example.LoyaltyBot.entity.User;
import com.example.LoyaltyBot.repository.UserRepository;
import com.example.LoyaltyBot.util.PasswordGenerator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService, PasswordGenerator passwordGenerator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.passwordGenerator = passwordGenerator;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserDto::toUserDto)
                .toList();
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public void editUser(UserDto userDto) {
        userRepository.save(userDto.toUser());
    }

    @Transactional
    public TemporaryPasswordResponse createUser(CreateUserDto userDto) {
        String password = passwordGenerator.generate();
        userRepository.save(userDto.toUser(roleService.findById(userDto.role_id()),
                passwordEncoder.encode(password)));
        return TemporaryPasswordResponse.forCreate(
                userDto.username(),
                password
        );
    }

    @Transactional
    public void toggleEnabled(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("нет юзера по id "));
        user.setEnabled(!user.getEnabled());
        userRepository.save(user);
    }

    public void changePassword(String newPassword) {
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("Пользователь не авторизован");
        }

        Object principal = auth.getPrincipal();
        if (!(principal instanceof User)) {
            throw new IllegalStateException("Неверный тип пользователя");
        }

        // ✅ Проверка: пароль не пустой
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }
        User user = (User) principal;
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setShouldChangePassword(false);
        userRepository.save(user);
    }

    public TemporaryPasswordResponse resetPassword(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("not found user"));

        String password = passwordGenerator.generate();
        user.setPassword(passwordEncoder.encode(password));
        user.setShouldChangePassword(true);
        userRepository.save(user);

        return TemporaryPasswordResponse.forReset(user.getUsername(),password);
    }
}
