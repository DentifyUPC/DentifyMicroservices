package com.upc.dentify.servicecatalogservice.infrastructure.persistence.security;

import com.upc.dentify.servicecatalogservice.infrastructure.persistence.security.GatewayHeaderAuthFilter;
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
                        .requestMatchers("/api/v1/acl-service-catalog/**").permitAll()
                        .requestMatchers("/api/v1/items/**", "/api/v1/items-per-services/**", "/api/v1/services/**", "/actuator/**").permitAll()
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