package com.wicc.personalassistant.repo.user;

import com.wicc.personalassistant.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Integer> {

    User getUserByEmail(String email);
}
