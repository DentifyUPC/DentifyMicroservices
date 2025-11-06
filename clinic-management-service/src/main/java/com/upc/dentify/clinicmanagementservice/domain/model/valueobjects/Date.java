package com.upc.dentify.clinicmanagementservice.domain.model.valueobjects;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public record Date(String date) {

    public static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Date {
        if (date == null || date.isBlank()) {
            throw new IllegalArgumentException("Date cannot be null or blank");
        }

        LocalDate parsedDate;

        try {
            parsedDate = LocalDate.parse(date, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Date must be a valid date in format DD/MM/YYYY");
        }

        if (parsedDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be before the current date");
        }

        if (parsedDate.isAfter(LocalDate.now().plusYears(1))) {
            throw new IllegalArgumentException("Date cannot be more than one year in the future");
        }
    }

}
