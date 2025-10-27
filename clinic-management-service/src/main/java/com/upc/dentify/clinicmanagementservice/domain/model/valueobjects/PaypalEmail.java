package com.upc.dentify.clinicmanagementservice.domain.model.valueobjects;

import java.util.regex.Pattern;

public record PaypalEmail(String paypalEmail) {

    public static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public PaypalEmail {
        if (paypalEmail == null || paypalEmail.isBlank()) {
            throw new IllegalArgumentException("Paypal email cannot be null or blank");
        }
        if (!EMAIL_PATTERN.matcher(paypalEmail).matches()) {
            throw new IllegalArgumentException("Invalid paypal email format");
        }
    }
}
