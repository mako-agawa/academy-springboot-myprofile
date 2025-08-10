package com.spring.springbootapplication.service;

import com.spring.springbootapplication.controller.UserController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class UserSecurityConfig {

    // private final UserController authController;

    // UserSecurityConfig(UserController authController) {
    //     this.authController = authController;
    // }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable());
        http.authorizeHttpRequests((authorize) -> {
            authorize.requestMatchers("/").permitAll().requestMatchers("/css/**").permitAll().requestMatchers("/js/**")
                    .permitAll().requestMatchers("/img/**").permitAll().anyRequest().authenticated();
        });
        http.formLogin(form -> {
            form.defaultSuccessUrl("/").permitAll();
        });

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailManager(){
        String username = "user";
        String pass = "pass";

        UserDetails user = User.withUsername(username).password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(pass))
                .roles("USER").build();
        return new InMemoryUserDetailsManager(user);
    }

}
