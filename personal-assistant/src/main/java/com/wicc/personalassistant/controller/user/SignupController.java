package com.wicc.personalassistant.controller.user;

import com.wicc.personalassistant.dto.ResponseDto;
import com.wicc.personalassistant.dto.user.UserDto;
import com.wicc.personalassistant.service.user.UserService;
import com.wicc.personalassistant.utils.EmailServiceUtil;
import com.wicc.personalassistant.utils.RandomNumberGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class SignupController {

    private final UserService userService;
    private final EmailServiceUtil emailServiceUtil;

    public SignupController(UserService userService, EmailServiceUtil emailServiceUtil) {
        this.userService = userService;
        this.emailServiceUtil = emailServiceUtil;
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "user/signup/signup";
    }
    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult bindingResult,
                             Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userDto", userDto);
            model.addAttribute("message", "Account creation failed");
            return "user/signup/signup";
        }
        //generate random 5 digit number
        String generate = RandomNumberGenerator.generate();
        emailServiceUtil.send(userDto.getEmail(),
                "Verification code for personal assistant",
                "Hi, "+ userDto.getName()+"Please enter this verification code to verify your email.\n" +
                        "Verification code: "+ generate);
        //saving the random number to session
        session.setAttribute("code", generate);
        //saving userDto to session
        session.setAttribute("userDto",userDto);
        model.addAttribute("userDto",new UserDto());
        return "user/verification/emailverification";
    }

    //save user after verification
    @PostMapping("/verify")
    public String verify(@ModelAttribute UserDto userDto,
                         HttpSession session,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        UserDto userDto1 = (UserDto)session.getAttribute("userDto");

        //getting random number from session and verify user input and session random data
        if (session.getAttribute("code").equals(userDto.getVerificationCode())) {
            //saving data to database
            ResponseDto save = userService.save(userDto1);
            redirectAttributes.addFlashAttribute("message", save.getMessage());
            return "redirect:/user/signup";
        } else {
            model.addAttribute("message", "Email verification failed");
            model.addAttribute("userDto",userDto1);
            return "user/signup/signup";
        }
    }
}
