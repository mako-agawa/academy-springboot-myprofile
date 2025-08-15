package com.spring.springbootapplication.service;

import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.repository.UserRepository;

@Service

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            System.out.println("===================");
            System.out.println(".getCurrentUser()  ");
            System.out.println("===================");
            return userRepository.findByEmail(email).orElseThrow();
        }
        throw new IllegalStateException();
    }
}