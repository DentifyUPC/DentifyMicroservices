package com.upc.dentify.clinicmanagementservice.domain.model.valueobjects;

import java.util.regex.Pattern;

public record ContactEmail(String contactEmail) {

    public static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public ContactEmail {
        if (contactEmail == null || contactEmail.isBlank()) {
            throw new IllegalArgumentException("Contact email cannot be null or blank");
        }
        if (!EMAIL_PATTERN.matcher(contactEmail).matches()) {
            throw new IllegalArgumentException("Invalid contact email format");
        }
    }
}

