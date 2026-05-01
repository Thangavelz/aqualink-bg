package com.aqualink.tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Expose BCryptPasswordEncoder as a Spring-managed bean.
     * Injected into CustomerAuthService and VendorAuthService via constructor.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Suppress Spring Boot's auto-generated UserDetailsService + random password.
     * We handle auth ourselves via X-CUSTOMER-ID / X-VENDOR-ID headers.
     * Without this bean, Spring Boot prints "Using generated security password"
     * and may interfere with BCrypt password matching.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        // Empty manager — no in-memory users. Our app uses custom header-based auth.
        return new InMemoryUserDetailsManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> {})
            .csrf(csrf -> csrf.disable())
            .formLogin(form -> form.disable())
            .httpBasic(httpBasic -> httpBasic.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/dev/**").permitAll()
                .requestMatchers("/", "/error").permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
    }
}