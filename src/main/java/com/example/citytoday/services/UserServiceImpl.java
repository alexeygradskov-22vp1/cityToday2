package com.example.citytoday.services;

import com.example.citytoday.exceptions.ResourceException;
import com.example.citytoday.models.Role;
import com.example.citytoday.models.User;
import com.example.citytoday.reps.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    private final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public String create(User user) throws AuthenticationException {
        if (userRepository.existsUserByLogin(user.getLogin())) {
            throw new BadCredentialsException("User already exists");
        }
        /*if (!user.getPassword()
                .matches("^(?=.*[A-Z]+)(?=.*\\d+)(?=.*[a-z]+)(?=.*[.,/!?]+).{8,15}$")) {
            throw new BadCredentialsException("Password don't valid:\n" +
                    "1 digit \n 1 special character \n 1 alphabetic \n min 8 characters");
        }*/
        user.setRoles(Collections.singleton(new Role(0, "ROLE_ADMIN")));
        user.setPassword(user.getPassword());
        user.setLocked(false);
        user.setRegDate(LocalDateTime.now());
        userRepository.save(user);
        return "";
    }

    public void toggleUserBlock(Long userId) {
        User user = userRepository.findById(Math.toIntExact(userId)).orElse(null);
        if (user != null) {
            user.setLocked(!user.isLocked());
            userRepository.save(user);
        }
    }


    public List<User> readAll() {
        return userRepository.findAll();
    }

    public User read(int id) {
        return userRepository.getOne(id);
    }


    public void update(User user) {
        userRepository.save(user);
    }


    public void delete(Long id) throws ResourceException {
        if (!userRepository.existsById(Math.toIntExact(id))) {
            throw new ResourceException("User not found");
        }
        userRepository.deleteById(Math.toIntExact(id));
    }

    public User getUserByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findUserByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь" + login + " не найден");
        }

        List<GrantedAuthority> grantedAuthorities;
        grantedAuthorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}

