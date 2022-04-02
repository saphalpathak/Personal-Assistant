package com.wicc.personalassistant.service.user;

import com.wicc.personalassistant.entity.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserDetailService {

    private final UserService userService;

    public CurrentUserDetailService(UserService userService) {
        this.userService = userService;
    }

    //getting the current username and find current user by email and get id of this user
    public Integer getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = null;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        }
        return userService.getUserByEmail(email).getId();
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
