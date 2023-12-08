package com.example.citytoday.configs.security;

import com.example.citytoday.models.User;
import com.example.citytoday.services.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class SessionFilterActivity extends OncePerRequestFilter {
    private final UserServiceImpl userService;
    Logger logger = LoggerFactory.getLogger(SessionFilterActivity.class);

    public SessionFilterActivity(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info(auth.getName());
        if (auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)){
            User user = userService.getUserByLogin(auth.getName());
            user.setLastActivityDate(LocalDateTime.now());
            userService.update(user);
        }

        filterChain.doFilter(request,response);
    }
}
