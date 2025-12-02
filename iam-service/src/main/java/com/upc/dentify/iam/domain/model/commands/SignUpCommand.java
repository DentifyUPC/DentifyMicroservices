package com.upc.dentify.iam.domain.model.commands;
import com.upc.dentify.iam.domain.model.validation.PasswordPolicy;

import java.util.List;
import java.util.stream.Collectors;


public record SignUpCommand(
        String firstName,
        String lastName,
        String username,
        String password,
        String birthDate,
        String email,
        Long identificationTypeId,
        Long roleId,
        Long clinicId
) {

    public SignUpCommand {
        if(firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("firstName is required");
        }

        if(lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("lastName is required");
        }

        if(username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        if(password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        List<String> pwdViolations = PasswordPolicy.validate(password);
        if (!pwdViolations.isEmpty()) {
            String message = pwdViolations.stream().collect(Collectors.joining(" "));
            throw new IllegalArgumentException("Password policy violations: " + message);
        }

        if(birthDate == null || birthDate.isBlank()) {
            throw new IllegalArgumentException("birthDate is required");
        }

        if(email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if(identificationTypeId == null || identificationTypeId <= 0) {
            throw new IllegalArgumentException("identificationTypeId cannot be null or zero or negative");
        }

        if(roleId == null || roleId <= 0) {
            throw new IllegalArgumentException("Role Id cannot be null or zero or negative");
        }

        if(clinicId == null || clinicId <= 0) {
            throw new IllegalArgumentException("Clinic Id cannot be null or zero or negative");
        }
    }
}
