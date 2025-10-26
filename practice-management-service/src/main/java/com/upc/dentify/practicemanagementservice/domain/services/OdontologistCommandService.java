package com.upc.dentify.practicemanagementservice.domain.services;

import com.upc.dentify.practicemanagementservice.domain.model.aggregates.Odontologist;
import com.upc.dentify.practicemanagementservice.domain.model.commands.UpdateOdontologistCommand;
import com.upc.dentify.practicemanagementservice.domain.model.events.UserCreatedEvent;
import com.upc.dentify.practicemanagementservice.domain.model.events.UserUpdatedEvent;

import java.util.Optional;

public interface OdontologistCommandService {
    Optional<Odontologist> handle(UpdateOdontologistCommand command);
    void handle(UserCreatedEvent event);
    void handle(UserUpdatedEvent event);
}