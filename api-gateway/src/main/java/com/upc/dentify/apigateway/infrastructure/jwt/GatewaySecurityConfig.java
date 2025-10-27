package com.upc.dentify.apigateway.infrastructure.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(GatewaySecurityConfig.class);

    @Value("${gateway.security.enabled:false}")
    private boolean securityEnabled;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        log.info("Gateway security enabled: {}", securityEnabled);

        if (!securityEnabled) {
            http
                    .csrf(csrf -> csrf.disable())
                    .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll());
            return http.build();
        }

        // Seguridad activa: desactivar httpBasic y activar resource-server JWT (HS256)
        http
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.setAllowedOriginPatterns(java.util.List.of("*"));
                    corsConfig.setAllowedMethods(java.util.List.of("GET","POST","PUT","DELETE","OPTIONS"));
                    corsConfig.setAllowedHeaders(java.util.List.of("*"));
                    corsConfig.setAllowCredentials(true);
                    return corsConfig;
                }))
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/iam-service/api/v1/auth/**", "/actuator/**", "/eureka/**").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                                .jwt(Customizer.withDefaults()));
        return http.build();
    }

    /**
     * Decoder para HS256 usando la misma secret que firma los tokens en el IAM.
     * jwt.secret debe coincidir exactamente con la secret del servicio de auth.
     */
    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        byte[] secretBytes = jwtSecret.getBytes();
        SecretKey key = new SecretKeySpec(secretBytes, "HmacSHA256");
        return NimbusReactiveJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
    }
}