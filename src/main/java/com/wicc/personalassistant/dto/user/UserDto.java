package com.wicc.personalassistant.dto.user;

import com.wicc.personalassistant.dto.transactioncategory.CategoryDto;
import com.wicc.personalassistant.entity.todo.Todo;
import com.wicc.personalassistant.entity.transaction.Transaction;
import com.wicc.personalassistant.entity.transactioncategory.Category;
import com.wicc.personalassistant.entity.user.User;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Integer id;

    @NotEmpty(message = "Name can be be empty!!")
    private String name;

    @Pattern(regexp = "^\\+?(?:977)?[ -]?(?:(?:(?:98|97)-?\\d{8})|(?:01-?\\d{7}))$",
            message = "Wrong mobile number or number must be of 10 digit ")
    @NotEmpty(message = "Contact must not be empty")
    private String contact;

    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Invalid Email !! please type a valid email.")
    @NotEmpty(message = "Email must not be empty")
    private String email;

    @NotEmpty(message = "Password can not be empty!!!")
    private String password;

    public UserDto(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.contact = user.getContact();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    //converting list of entities to dtos
    public static List<UserDto> toDtos(List<User> users){
        List<UserDto> userDtos = new ArrayList<>();
        users.forEach(user-> userDtos.add(new UserDto(user)));
        return userDtos;
    }


}
