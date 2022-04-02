package com.wicc.personalassistant.service.user;

import com.wicc.personalassistant.dto.ResponseDto;
import com.wicc.personalassistant.dto.user.UserDto;
import com.wicc.personalassistant.entity.user.User;
import com.wicc.personalassistant.repo.user.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepo userRepo;


    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepo userRepo) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepo = userRepo;
    }

    @Override

    //save new user
    //return response as a responseDto with result
    public ResponseDto save(UserDto userDto) {
        try{
            User user = new User(userDto);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user = userRepo.save(user);
            return new ResponseDto(true,"Account created successfully!!",user);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseDto(false,"Account creation failed.",null);
        }
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.getUserByEmail(email);
    }

}
