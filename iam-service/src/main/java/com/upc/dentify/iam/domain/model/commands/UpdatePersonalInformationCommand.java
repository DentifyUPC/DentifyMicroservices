package com.upc.dentify.iam.domain.model.commands;

public record UpdatePersonalInformationCommand(String firstName, String lastName) {

    public UpdatePersonalInformationCommand {
        if(firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("firstName is required");
        }
        if(lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("lastName is required");
        }
    }
}
