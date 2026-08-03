package com.example.LoyaltyBot.config;

import com.example.LoyaltyBot.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
public class SecurityConfig {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthenticationSuccessHandler customSuccessHandler;

    public SecurityConfig(UserService userService,
                          PasswordEncoder passwordEncoder,
                          CustomAuthenticationSuccessHandler customSuccessHandler
    ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.customSuccessHandler = customSuccessHandler;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/users/change_password").authenticated()
                        .requestMatchers("/auth/login")
                        .permitAll()
                        .anyRequest().hasAnyRole("USER", "ADMIN"))
                .formLogin((form) -> form.loginPage("/auth/login")
                        .loginProcessingUrl("/perform-login")
                        .successHandler(customSuccessHandler)
                        .failureUrl("/auth/login-error")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/auth/login"));
        return http.build();
    }
}
