package com.aqualink.tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // ✅ Enable CORS
            .cors(cors -> {})

            // ❌ Disable CSRF (required for API)
            .csrf(csrf -> csrf.disable())

            // ❌ Disable login page completely
            .formLogin(form -> form.disable())

            // ❌ Disable HTTP Basic (optional)
            .httpBasic(httpBasic -> httpBasic.disable())

            // ✅ Authorization rules
            .authorizeHttpRequests(auth -> auth

                // allow preflight requests (VERY IMPORTANT)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // allow all API endpoints (DEV MODE)
                .requestMatchers("/api/**").permitAll()

                // allow error & root
                .requestMatchers("/", "/error").permitAll()

                // everything else blocked (or change later)
                .anyRequest().authenticated()
            );

        return http.build();
    }
}