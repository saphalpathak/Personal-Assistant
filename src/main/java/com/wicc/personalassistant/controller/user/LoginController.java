package com.wicc.personalassistant.controller.user;

import com.wicc.personalassistant.dto.user.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class LoginController {

    @GetMapping("/login")
    public String login(Model model){
        return "user/login/login";
    }
}
