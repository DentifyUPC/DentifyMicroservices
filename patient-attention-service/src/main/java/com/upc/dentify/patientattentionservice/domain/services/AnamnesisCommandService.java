package com.upc.dentify.patientattentionservice.domain.services;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.Anamnesis;
import com.upc.dentify.patientattentionservice.domain.model.commands.UpdateAnamnesisCommand;

import java.util.Optional;

public interface AnamnesisCommandService {
    Optional<Anamnesis> handle(UpdateAnamnesisCommand command);
}
