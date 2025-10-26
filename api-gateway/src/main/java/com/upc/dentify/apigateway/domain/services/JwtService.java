package com.upc.dentify.apigateway.domain.services;


import io.jsonwebtoken.Claims;

import java.util.List;

public interface JwtService {
    String extractUsername(String token);
    List<String> extractRoles(String token);
    boolean validateToken(String token);
    Claims extractAllClaims(String token);

}
