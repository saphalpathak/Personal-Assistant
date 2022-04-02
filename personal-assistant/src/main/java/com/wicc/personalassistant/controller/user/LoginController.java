package com.wicc.personalassistant.controller.user;

import com.wicc.personalassistant.dto.user.UserDto;
import com.wicc.personalassistant.entity.user.User;
import com.wicc.personalassistant.service.user.UserService;
import com.wicc.personalassistant.utils.EmailServiceUtil;
import com.wicc.personalassistant.utils.RandomNumberGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class LoginController {

    private final EmailServiceUtil emailServiceUtil;
    private final UserService userService;

    public LoginController(EmailServiceUtil emailServiceUtil, UserService userService) {
        this.emailServiceUtil = emailServiceUtil;
        this.userService = userService;
    }


    @GetMapping("/login")
    public String login(Model model) {
        return "user/login/login";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "user/forgotpassword/emailinput";
    }

    @PostMapping("/send-otp")
    public String verifyEmail(@ModelAttribute UserDto userDto,
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {
        User userByEmail = userService.getUserByEmail(userDto.getEmail());
        //saving data of user to session
        session.setAttribute("user",userByEmail);
        if (userByEmail != null) {
            //generate random number
            String otp = RandomNumberGenerator.generate();
            session.setAttribute("otp",otp);
            emailServiceUtil.send(userDto.getEmail(),
                    "OTP to change password",
                    "Please enter this otp code to change password.\n" +
                            "OTP code: " + otp);
            return "user/forgotpassword/verifyotp";
        } else {
            redirectAttributes.addFlashAttribute("message","Your email is not registered yet!!");
            return "redirect:/user/forgot-password";
        }

    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@ModelAttribute UserDto userDto,
                            HttpSession session,
                            RedirectAttributes redirectAttributes,
                            Model model){
        //verifying the otp using session
       if(session.getAttribute("otp").equals(userDto.getVerificationCode())){
           model.addAttribute("userDto",new UserDto());
           return "user/forgotpassword/changepassword";
       }else{
           redirectAttributes.addFlashAttribute("message","Otp does not match!!");
           return "redirect:/user/forgot-password";
       }
    }

    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute UserDto userDto ,
                                 RedirectAttributes redirectAttributes,
                                 HttpSession session){
        //getting current user form session
        UserDto userDto1 = new UserDto((User)session.getAttribute("user"));
        //setting new password
        userDto1.setPassword(userDto.getPassword());
        userService.save(userDto1);
        redirectAttributes.addFlashAttribute("message","Password successfully changed");
        return "redirect:/user/login";
    }

}
