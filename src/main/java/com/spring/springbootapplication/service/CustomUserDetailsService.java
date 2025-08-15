package com.spring.springbootapplication.service;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(User user) {
        
        user.setPasswordDigest(passwordEncoder.encode(user.getPasswordDigest()));
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        System.out.println("===================");
        System.out.println("loadUserByUsername()");
        System.out.println("===================");

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), // ← メールアドレスを username として扱う
                user.getPasswordDigest(),
                new ArrayList<>());
    }

}