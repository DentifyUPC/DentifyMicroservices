package com.upc.dentify.iam.domain.model.valueobjects;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public record BirthDate(String birthDate) {

    public static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public BirthDate {
        if (birthDate == null || birthDate.isBlank()) {
            throw new IllegalArgumentException("Birth date cannot be null or blank");
        }

        try {
            LocalDate.parse(birthDate, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Birth date must be a valid date in format DD/MM/YYYY");
        }
    }

}
