package com.wicc.personalassistant.controller.user;
import com.wicc.personalassistant.dto.ResponseDto;
import com.wicc.personalassistant.dto.user.UserDto;
import com.wicc.personalassistant.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signup(Model model){
        model.addAttribute("userDto",new UserDto());
        return "user/signup/signup";
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult bindingResult,
                             RedirectAttributes attributes){
        if(bindingResult.hasErrors()){
            attributes.addFlashAttribute("userDto",userDto);
            attributes.addFlashAttribute("message","Account creation failed");
            return "user/signup/signup";
        }
        ResponseDto save = userService.save(userDto);
        attributes.addFlashAttribute("message",save.getMessage());
        return "redirect:/user/signup";
    }
}
