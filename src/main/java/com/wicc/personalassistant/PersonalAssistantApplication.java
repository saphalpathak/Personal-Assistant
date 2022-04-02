package com.wicc.personalassistant;

import com.wicc.personalassistant.dto.user.UserDto;
import com.wicc.personalassistant.service.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PersonalAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonalAssistantApplication.class, args);
    }
}
