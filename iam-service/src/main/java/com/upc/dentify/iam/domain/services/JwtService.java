package com.upc.dentify.iam.domain.services;

import com.upc.dentify.iam.domain.model.aggregates.User;
import org.springframework.security.core.userdetails.UserDetails;


public interface JwtService {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    String extractUsername(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
}
