package com.upc.dentify.practicemanagementservice.infrastructure.persistence.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final GatewayHeaderAuthFilter gatewayHeaderAuthFilter;
    private final InternalServiceAuthFilter internalServiceAuthFilter;

    public SecurityConfig(GatewayHeaderAuthFilter gatewayHeaderAuthFilter,
                          InternalServiceAuthFilter internalServiceAuthFilter) {
        this.gatewayHeaderAuthFilter = gatewayHeaderAuthFilter;
        this.internalServiceAuthFilter = internalServiceAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("api/v1/acl/**").permitAll()
                        .requestMatchers("/api/v1/patients/**", "/actuator/**").permitAll()
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(internalServiceAuthFilter, UsernamePasswordAuthenticationFilter.class);
        // Register our filter early in the chain
        http.addFilterAfter(gatewayHeaderAuthFilter, InternalServiceAuthFilter.class);

        // No sessions
        http.sessionManagement(session -> session.sessionCreationPolicy(
                org.springframework.security.config.http.SessionCreationPolicy.STATELESS));

        return http.build();
    }
}