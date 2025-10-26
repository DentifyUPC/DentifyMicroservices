package com.upc.dentify.practicemanagementservice.domain.model.valueobjects;

public record PersonName(String firstName, String lastName) {

    public PersonName() {
        this(null, null);
    }

    public PersonName {
        if(firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("FirstName is required");
        }
        if(lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("LastName is required");
        }
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}