package com.upc.dentify.patientattentionservice.infrastructure.persistence.crypto;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class CryptoConfig {
    @Value("${app.crypto.key:}")
    private String appKeyBase64;


    @PostConstruct
    public void init() {
        if (appKeyBase64 == null || appKeyBase64.isBlank()) {
            throw new IllegalStateException("APP_DATA_KEY / app.crypto.key must be set");
        }
        byte[] keyBytes = Base64.getDecoder().decode(appKeyBase64);
        if (keyBytes.length != 32) throw new IllegalStateException("Key must be 32 bytes (base64-encoded)");


        CryptoService.initWithKeyBytes(keyBytes);
    }
}