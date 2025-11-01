package com.upc.dentify.clinicmanagementservice.infrastructure.persistence.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class InternalServiceAuthFilter extends OncePerRequestFilter {
    private static final String INTERNAL_HEADER = "X-Internal-Token";
    @Value("${internal.service.token}")
    private String INTERNAL_SECRET;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().contains("/api/v1/acl/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader(INTERNAL_HEADER);

        if (token == null || !token.equals(INTERNAL_SECRET)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access Denied: Invalid internal service token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}