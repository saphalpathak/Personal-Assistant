package com.wicc.personalassistant.config.security;

import com.wicc.personalassistant.entity.user.User;
import com.wicc.personalassistant.repo.user.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    //checking into database if email exists or not
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.getUserByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("User Not Found!!");
        }
        return new CustomUserDetails(user);
    }
}
