package com.upc.dentify.iam.messaging;

import com.upc.dentify.iam.domain.events.UserCreatedEvent;

public class UserCreatedDomainEvent {
    private final UserCreatedEvent payload;
    public UserCreatedDomainEvent(UserCreatedEvent payload) { this.payload = payload; }
    public UserCreatedEvent getPayload() { return payload; }
}

