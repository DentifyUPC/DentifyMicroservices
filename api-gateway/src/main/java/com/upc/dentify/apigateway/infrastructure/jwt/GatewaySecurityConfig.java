package com.upc.dentify.apigateway.infrastructure.jwt;

//import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.beans.factory.annotation.Value;


@Configuration
@EnableWebFluxSecurity

public class GatewaySecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(GatewaySecurityConfig.class);

    @Value("${gateway.security.enabled:false}")
    private boolean securityEnabled;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        log.info("Gateway security enabled: {}", securityEnabled);

        if (!securityEnabled) {
            // DESACTIVAR TODO (dev only)
            http.csrf(csrf -> csrf.disable())
                    .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll());
            return http.build();
        }

        // Comportamiento por defecto (si lo activas)
        http.csrf(csrf -> csrf.disable())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/iam-service/api/v1/auth/**").permitAll()
                        .anyExchange().authenticated());
        return http.build();
    }
}