package com.example.citytoday.configs.security;

import com.example.citytoday.security.AuthProvider;
import com.example.citytoday.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthProvider authProvider;
    private final UserServiceImpl userService;


    @Autowired
    public SecurityConfig(AuthProvider authProvider, UserServiceImpl userService) {
        this.authProvider = authProvider;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.
                csrf(AbstractHttpConfigurer::disable).
                authorizeHttpRequests(
                        (requests) -> requests.
                                requestMatchers(
                                        antMatcher("/css/**"),
                                        antMatcher("/js/**"),
                                        antMatcher("/"),
                                        antMatcher("/main"),
                                        antMatcher("/news/approve/**"),
                                        antMatcher("/news/delete/**"),
                                        antMatcher("/pageNotFound"),
                                        antMatcher("/registration"),
                                        antMatcher("/images/**"),
                                        antMatcher("/time")).
                                permitAll().
                                requestMatchers(
                                        antMatcher("/logout")).
                                authenticated().
                                requestMatchers(
                                        antMatcher("/administrate"),
                                        antMatcher("/users/deleteUser/**"),
                                        antMatcher("/users/blockUser/**"),
                                        antMatcher("/users")).
                                hasRole("ADMIN").
                                requestMatchers(
                                        antMatcher("/moderate")
                                ).hasAnyRole("ADMIN", "MODER").
                                requestMatchers(antMatcher("/users/**")).authenticated().
                                requestMatchers(
                                        antMatcher("/boards"),
                                        antMatcher("/news/write"),
                                        antMatcher("/news/**"),
                                        antMatcher("/boards/create"),
                                        antMatcher("/boards/**"),
                                        antMatcher("/boards/delete/**"),
                                        antMatcher("/deleteAccount/**")).
                                hasAnyRole("ADMIN", "USER")).formLogin(httpSecurityFormLoginConfigurer ->
                        httpSecurityFormLoginConfigurer.loginPage("/login").defaultSuccessUrl("/main", true).permitAll()).
                cors(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder noOpPasswordEncoder)
            throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(noOpPasswordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder noOpPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
