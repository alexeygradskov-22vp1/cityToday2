package com.example.citytoday.reps;

import com.example.citytoday.models.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByLogin(String login);
    boolean existsUserByLogin(String login);
}
