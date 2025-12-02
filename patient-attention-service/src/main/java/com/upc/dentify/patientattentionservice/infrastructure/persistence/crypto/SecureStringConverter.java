package com.upc.dentify.patientattentionservice.infrastructure.persistence.crypto;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Converter
public class SecureStringConverter implements AttributeConverter<String, String> {
    private static final Logger log = LoggerFactory.getLogger(SecureStringConverter.class);

    private CryptoService crypto() {
        return CryptoService.getInstance();
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        try {
            return crypto().encryptBase64(attribute);
        } catch (Exception e) {
            log.error("SecureStringConverter: fallo al encriptar", e);
            throw new RuntimeException("encrypt failed", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;

        if (!looksBase64(dbData)) {
            log.warn("SecureStringConverter: valor no parece cifrado, devolviendo texto plano para migración.");
            return dbData;
        }

        try {
            return crypto().decryptBase64(dbData);
        } catch (IllegalArgumentException iae) {
            log.warn("SecureStringConverter: IllegalArgumentException al descifrar, fallback a valor DB (posible dato sin cifrar).", iae);
            return dbData;
        } catch (Exception e) {
            log.warn("SecureStringConverter: fallo al descifrar, fallback a valor DB (posible dato sin cifrar).", e);
            return dbData;
        }
    }

    private boolean looksBase64(String s) {
        if (s == null) return false;
        return s.matches("^[A-Za-z0-9+/]+={0,2}$");
    }
}
