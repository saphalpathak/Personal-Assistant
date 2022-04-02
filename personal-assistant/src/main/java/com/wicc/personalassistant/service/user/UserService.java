package com.wicc.personalassistant.service.user;

import com.wicc.personalassistant.dto.ResponseDto;
import com.wicc.personalassistant.dto.user.UserDto;
import com.wicc.personalassistant.entity.user.User;
import com.wicc.personalassistant.service.GenericCrudService;

public interface UserService{
    ResponseDto save(UserDto userDto);

    public User getUserByEmail(String email);
}
