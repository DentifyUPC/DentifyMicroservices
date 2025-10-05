package com.upc.dentify.iam.domain.model.valueobjects;

import com.upc.dentify.iam.domain.model.entities.IdentificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Username {

    @Column(name = "username", nullable = false, unique = true)
    private String value;

    protected Username() {}

    public Username(String value) {
        this.value = value;
    }

    public Username(String value, IdentificationType type) {
        validate(value, type);
        this.value = value;
    }

    private void validate(String value, IdentificationType type) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }

        String typeName = type.getName().toUpperCase();

        switch (typeName) {
            case "DNI" -> {
                if (!value.matches("\\d{8}")) {
                    throw new IllegalArgumentException("DNI must contain exactly 8 digits");
                }
            }
            case "FOREIGNER ID CARD" -> {
                if (!value.matches("\\d{12,20}")) {
                    throw new IllegalArgumentException("Foreign id card must contain from 12 to 20 digits");
                }
            }
            default -> throw new IllegalArgumentException("Unsupported IdentificationType: " + typeName);
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}