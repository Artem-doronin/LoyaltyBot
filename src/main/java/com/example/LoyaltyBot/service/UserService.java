package com.example.LoyaltyBot.service;

import com.example.LoyaltyBot.dto.user.CreateUserDto;
import com.example.LoyaltyBot.dto.user.ChangePasswordRequest;
import com.example.LoyaltyBot.dto.user.TemporaryPasswordResponse;
import com.example.LoyaltyBot.dto.user.UserDto;
import com.example.LoyaltyBot.entity.User;
import com.example.LoyaltyBot.repository.UserRepository;
import com.example.LoyaltyBot.util.PasswordGenerator;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
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

    public TemporaryPasswordResponse createUser(CreateUserDto userDto) {
        String password = passwordGenerator.generate();
        userRepository.save(userDto.toUser(roleService.findById(userDto.role_id()),
                passwordEncoder.encode(password)));
        return TemporaryPasswordResponse.toDto(
                userDto.username(),
                password
        );
    }

    public void toggleEnabled(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("нет юзера по id "));
        user.setEnabled(!user.getEnabled());
        userRepository.save(user);
    }

    public void changePassword(ChangePasswordRequest dto) {
        User user = getCurrentUser();

        if (!passwordEncoder.matches(dto.oldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Неверный старый пароль");
        }

        validatePassword(dto.newPassword(), dto.confirmPassword());

        if (dto.oldPassword().equals(dto.newPassword())) {
            throw new IllegalArgumentException("Новый пароль должен отличаться от старого");
        }

        validatePassword(dto.newPassword(), dto.confirmPassword());
        user.setPassword(passwordEncoder.encode(dto.newPassword()));
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

        return TemporaryPasswordResponse.toDto(user.getUsername(), password);
    }

    public User getCurrentUser() {
        log.info("getCurrentUser() - получение текущего пользователя");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("Пользователь не авторизован");
        }

        log.info("Authentication: {} isAuthenticated: {} Имя пользователя: {} Authorities: {} ",
                auth.getClass().getSimpleName(),
                auth.isAuthenticated(),
                auth.getName(),
                auth.getAuthorities());

        Object principal = auth.getPrincipal();
        if (!(principal instanceof User)) {
            throw new IllegalStateException("Неверный тип пользователя");
        }
        return (User) principal;
    }

    private void validatePassword(String password, String confirmPassword) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Пароли не совпадают");
        }
    }
}
