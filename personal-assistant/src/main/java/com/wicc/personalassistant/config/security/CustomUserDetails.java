package com.wicc.personalassistant.config.security;

import com.wicc.personalassistant.entity.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    //getting roles
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //no multiple user so "USER" default user
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    //getting password
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    //getting username
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
