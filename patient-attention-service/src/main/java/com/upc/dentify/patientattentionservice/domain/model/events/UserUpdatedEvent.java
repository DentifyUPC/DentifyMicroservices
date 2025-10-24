package com.upc.dentify.patientattentionservice.domain.model.events;

import lombok.Getter;

@Getter
public class UserUpdatedEvent {
    private final Long userId;
    private final String firstName;
    private final String lastName;

    public UserUpdatedEvent(Long userId, String firstName, String lastName) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
