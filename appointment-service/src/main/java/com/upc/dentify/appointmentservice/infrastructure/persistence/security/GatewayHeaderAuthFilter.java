package com.upc.dentify.appointmentservice.infrastructure.persistence.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GatewayHeaderAuthFilter extends OncePerRequestFilter {

    public static final String HEADER_USER_ID = "X-User-Id";
    public static final String HEADER_USERNAME = "X-Username";
    public static final String HEADER_ROLES = "X-Role";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // If there's already an authentication, skip
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String userIdHeader = request.getHeader(HEADER_USER_ID);
            String username = request.getHeader(HEADER_USERNAME);
            String rolesHeader = request.getHeader(HEADER_ROLES);

            if (userIdHeader != null && username != null) {
                // parse roles: could be "ROLE_DENTIST,ROLE_ADMIN" or a list-looking string
                List<SimpleGrantedAuthority> authorities = List.of();
                if (rolesHeader != null && !rolesHeader.isBlank()) {
                    // try to split by comma, strip brackets, etc.
                    String cleaned = rolesHeader.replaceAll("[\\[\\]]", "");
                    authorities = Arrays.stream(cleaned.split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                }

                // We set principal as username (you can create a custom Principal object if you want userId too)
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                // Optionally store userId as details so services can access it:
                auth.setDetails(userIdHeader);

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}