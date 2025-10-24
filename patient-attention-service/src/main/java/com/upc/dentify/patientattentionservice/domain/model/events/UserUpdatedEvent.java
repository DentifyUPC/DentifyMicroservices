package com.upc.dentify.patientattentionservice.domain.model.events;

import lombok.Getter;

@Getter
public class UserUpdatedEvent {
    private final Long userId;
    private final String firstName;
    private final String lastName;
    private final Long role;

    public UserUpdatedEvent(Long userId, String firstName, String lastName, Long role) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }
}
