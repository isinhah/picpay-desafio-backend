package com.desafio.picpay_simplificado.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize

                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/{userId}").hasAnyRole("USER", "MERCHANT", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/{userId}").hasAnyRole("USER", "MERCHANT", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{userId}").hasAnyRole("USER", "MERCHANT", "ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/transactions/user/{userId}").hasAnyRole("USER", "MERCHANT", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/transactions/transfer").hasAnyRole("USER")

                        .requestMatchers(HttpMethod.GET, "/api/users/{userId}/wallet").hasAnyRole("USER", "MERCHANT", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/users/{userId}/wallet/deposit").hasAnyRole("USER", "MERCHANT")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}