package com.upc.dentify.practicemanagementservice.domain.model.events;

import lombok.Getter;

@Getter
public class UserCreatedEvent {
    private final Long userId;
    private final Long role;
    private final String firstName;
    private final String lastName;
    private final String birthDate;
    private final String email;
    private final Long clinicId;

    public UserCreatedEvent(Long userId, Long role, String firstName, String lastName,
                            String birthDate, String email, Long clinicId) {
        this.userId = userId;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
        this.clinicId = clinicId;
    }
}