package com.hrms.security;


import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.hrms.users.JwtFilter.JwtFilter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .cors(cors -> {})   // ENABLE CORS
            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/api/auth/**").permitAll()
//                .anyRequest().authenticated()
//            )
            
            .authorizeHttpRequests(auth -> auth

            	    // Public endpoints
            	    .requestMatchers("/api/auth/**").permitAll()

            	    // ADMIN full control
            	    .requestMatchers("/api/admin/**").hasRole("ADMIN")

            	    // Employee management
            	    .requestMatchers("/api/employees/add")
            	        .hasAnyRole("ADMIN", "HR")

            	    .requestMatchers("/api/employees/delete/**")
            	        .hasAnyRole("ADMIN", "HR")

            	    .requestMatchers("/api/employees/all")
            	        .hasAnyRole("ADMIN", "HR", "MANAGER", "TEAM_LEAD")

            	    // Payroll section
            	    .requestMatchers("/api/payroll/**")
            	        .hasAnyRole("ADMIN", "PAYROLL_EXECUTIVE")

            	    // IT Support section
            	    .requestMatchers("/api/it/**")
            	        .hasAnyRole("ADMIN", "IT_SUPPORT")
            	        
            	     // Attendance section
            	        .requestMatchers("/api/attendance/check-in", "/api/attendance/check-out")
            	            .hasRole("EMPLOYEE")

            	        .requestMatchers("/api/attendance/hr-report", "/api/attendance/dashboard-summary")
            	            .hasAnyRole("HR", "ADMIN")

            	        .requestMatchers("/api/attendance/summary", "/api/attendance/monthly-summary", "/api/attendance/**")
            	            .authenticated()

            	    // Everything else requires login
            	    .anyRequest().authenticated()
            	)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
 // ✅ CORS CONFIGURATION
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
    

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}