package com.upc.dentify.apigateway.infrastructure.jwt;

import com.upc.dentify.apigateway.domain.services.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.ws.rs.core.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.core.Ordered;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered{

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtService jwtService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // rutas públicas que NO requieren token
    private final List<String> publicPaths = List.of(
            "/iam-service/api/v1/auth/**",
            "/clinic-management-service/api/v1/clinics/clinics-information-pre-register",
            "/eureka/**",        // comunicación con eureka
            "/actuator/**"      // health checks
    );


    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();


        // permitir rutas públicas
        // Si coincide con públicas, dejar pasar sin tocar body ni headers
        for (String p : publicPaths) {
            if (pathMatcher.match(p, path)) {
                log.debug("Public path detected, skipping JWT validation: {}", path);
                log.debug("Gateway checking path: {}", path);
                return chain.filter(exchange);
            }
        }

        // Para rutas protegidas: validar Authorization
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        if (!jwtService.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // token válido -> extraer claims y añadir cabeceras al request para downstream
        Claims claims = jwtService.extractAllClaims(token);
        String username = claims.getSubject();
        Object userId = claims.get("userId");
        Object roles = claims.get("role");

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-Username", (username != null) ? username : "")
                .header("X-User-Id", (userId != null) ? String.valueOf(userId) : "")
                .header("X-Role", (roles != null) ? String.valueOf(roles) : "")
                .build();

        ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        return -100; // ejecutar temprano
    }
}
