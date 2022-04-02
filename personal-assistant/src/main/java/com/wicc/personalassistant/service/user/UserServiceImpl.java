package com.wicc.personalassistant.service.user;

import com.wicc.personalassistant.dto.ResponseDto;
import com.wicc.personalassistant.dto.user.UserDto;
import com.wicc.personalassistant.entity.user.User;
import com.wicc.personalassistant.repo.user.UserRepo;
import com.wicc.personalassistant.utils.EmailServiceUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepo userRepo;
    private final EmailServiceUtil emailServiceUtil;


    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepo userRepo, EmailServiceUtil emailServiceUtil) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepo = userRepo;
        this.emailServiceUtil = emailServiceUtil;
    }

    @Override

    //save new user
    //return response as a responseDto with result
    public ResponseDto save(UserDto userDto) {
        try{
            User user = new User(userDto);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user = userRepo.save(user);
//            emailServiceUtil.send(user.getEmail(),"Added as an user","You are added as a user in Personal Accountant!!!");
            return new ResponseDto(true,"Account created successfully!!",user);
        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
            return new ResponseDto(false,"Account creation failed.",null);
        }
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.getUserByEmail(email);
    }

}
