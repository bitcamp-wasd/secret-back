package com.example.user.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@NoArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private String email;
    private Long userId;
    private String role;


    public CustomOAuth2User(String email, Long userId, String role) {
        this.email = email;
        this.userId = userId;
        this.role = role;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.singletonMap("email", email);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getName() {
        return email;
    }

    public Long getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }
}
