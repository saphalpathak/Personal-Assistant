package com.wicc.personalassistant.utils;

import com.wicc.personalassistant.entity.user.User;
import com.wicc.personalassistant.service.user.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class CurrentUserDetailService {

    private final UserService userService;

    public CurrentUserDetailService(UserService userService) {
        this.userService = userService;
    }

    //getting the current user id
    public Integer getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = null;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        }
        return userService.getUserByEmail(email);
    }
}
