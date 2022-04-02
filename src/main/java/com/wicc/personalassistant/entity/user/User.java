package com.wicc.personalassistant.entity.user;

import com.wicc.personalassistant.dto.user.UserDto;
import com.wicc.personalassistant.entity.todo.Todo;
import com.wicc.personalassistant.entity.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "users",
        uniqueConstraints ={
                @UniqueConstraint(name = "unique_user_contact",columnNames = "contact_no"),
                @UniqueConstraint(name = "unique_user_email",columnNames = "email")
        }
)
public class User {
    @Id
    @SequenceGenerator(name = "user_id_GEN",sequenceName = "user_id_GEN",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_id_GEN")
    private Integer id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "contact_no",nullable = false)
    private  String contact;

    @Column(name = "email",nullable = false)
    private String email;

    @Column(name = "password",nullable = false)
    private String password;


    //converting dto to entity
    public User(UserDto userDto){
        this.id = userDto.getId();
        this.name = userDto.getName();
        this.contact = userDto.getContact();
        this.email = userDto.getEmail();
        this.password = userDto.getPassword();
    }



}
