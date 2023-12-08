package com.example.citytoday.controllers;

import com.example.citytoday.exceptions.ResourceException;
import com.example.citytoday.models.User;
import com.example.citytoday.services.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Objects;

@Controller
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String getRegisterPage(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String register(@ModelAttribute("userForm") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", Objects.requireNonNull(bindingResult.getFieldError()).getRejectedValue());
        }
        try {
            userService.create(user);
        } catch (BadCredentialsException e) {
            return "registration";
        }
        return "redirect:/";
    }

    @GetMapping("/users/{username}")
    public String getProfilePage(@PathVariable String username, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByLogin(username);
        model.addAttribute("user", user);


        return "profile";
    }

    @GetMapping("/administrate")
    public String getAdminPage(Model model){
        model.addAttribute("users", userService.readAll());
        return "administrate";
    }

    @PostMapping("/users/block/{userId}")
    public String blockUser(@PathVariable("userId") Long userId){
        userService.toggleUserBlock(userId);
        return "redirect:/administrate";
    }

    @PostMapping("/users/delete/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId) throws ResourceException {
        userService.delete(userId);
        return "redirect:/administrate";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("userForm", new User());
        return "login";
    }


}
