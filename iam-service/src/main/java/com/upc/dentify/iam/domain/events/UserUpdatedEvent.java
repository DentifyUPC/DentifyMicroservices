package com.upc.dentify.iam.domain.events;

public record UserUpdatedEvent(
        Long userId,
        String firstName,
        String lastName,
        String email,
        String birthdate
) {
}
