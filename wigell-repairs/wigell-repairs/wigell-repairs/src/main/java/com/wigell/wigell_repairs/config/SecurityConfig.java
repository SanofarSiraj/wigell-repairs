package com.wigell.wigell_repairs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService users() {
        UserDetails user1 = User.withUsername("user1").password("{noop}password").roles("USER").build();
        UserDetails user2 = User.withUsername("user2").password("{noop}password").roles("USER").build();
        UserDetails user3 = User.withUsername("user3").password("{noop}password").roles("USER").build();
        UserDetails admin = User.withUsername("admin").password("{noop}adminpass").roles("ADMIN").build();
        return new InMemoryUserDetailsManager(user1, user2, user3, admin);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/wigellrepairs/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .httpBasic();
        return http.build();
    }
}
