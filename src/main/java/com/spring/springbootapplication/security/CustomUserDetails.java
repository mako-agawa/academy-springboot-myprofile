// src/main/java/com/spring/springbootapplication/security/CustomUserDetails.java
package com.spring.springbootapplication.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.spring.springbootapplication.entity.User;

public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) { this.user = user; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 役割テーブルが無い想定なので固定で USER 付与
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() { return user.getPasswordDigest(); }

    @Override
    public String getUsername() { return user.getEmail(); } // ← username=メール

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public Long getId() { return user.getId(); }
    public String getDisplayName() { return user.getName(); }
}