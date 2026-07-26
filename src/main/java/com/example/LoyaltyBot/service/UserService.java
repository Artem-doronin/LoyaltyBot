package com.example.LoyaltyBot.service;

import com.example.LoyaltyBot.dto.user.UserDto;
import com.example.LoyaltyBot.entity.User;
import com.example.LoyaltyBot.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public List<UserDto> findAll(){
        return  userRepository.findAll().stream()
                .map(UserDto::toUserDto)
                .toList();
    }

    public void deleteById(Long id){
        userRepository.deleteById(id);
    }

    public void editUser(UserDto userDto){
        userRepository.save(userDto.toUser());
    }

    public void createUser(UserDto userDto){


    }

}
