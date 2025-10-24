package com.upc.dentify.iam.messaging;

import com.upc.dentify.iam.domain.events.UserUpdatedEvent;

public class UserUpdatedDomainEvent {
    private final UserUpdatedEvent payload;
    public UserUpdatedDomainEvent(UserUpdatedEvent payload) { this.payload = payload; }
    public UserUpdatedEvent getPayload() { return payload; }
}
