package com.upc.dentify.patientattentionservice.domain.model.valueobjects;

import java.util.regex.Pattern;

public record PhoneNumber(String phoneNumber) {

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9]{9,15}$"); // solo números, 9-15 dígitos

    public PhoneNumber {
        if (phoneNumber != null && !phoneNumber.isBlank()) {
            if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
                throw new IllegalArgumentException("Invalid phone number format. Must be between 9 and 15 digits.");
            }
        } else {
            phoneNumber = null;
        }
    }

    public boolean isPresent() {
        return phoneNumber != null;
    }
}
