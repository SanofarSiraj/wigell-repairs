package com.wigell.wigell_repairs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService users() {
        UserDetails user1 = User.withUsername("sven1").password("{noop}sven1").roles("USER").build();
        UserDetails user2 = User.withUsername("sven2").password("{noop}sven2").roles("USER").build();
        UserDetails user3 = User.withUsername("sven3").password("{noop}sven3").roles("USER").build();
        UserDetails admin = User.withUsername("admin").password("{noop}admin").roles("ADMIN").build();
        return new InMemoryUserDetailsManager(user1, user2, user3, admin);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers(headers -> headers.frameOptions().sameOrigin()) // H2 console
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/wigellrepairs/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/wigellrepairs/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().permitAll()
                )
                .httpBasic();
        return http.build();
    }
}
