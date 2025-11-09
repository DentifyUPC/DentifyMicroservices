package com.upc.dentify.patientattentionservice.domain.services;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.OdontogramItem;
import com.upc.dentify.patientattentionservice.domain.model.commands.UpdateOdontogramItemCommand;

import java.util.Optional;

public interface OdontogramItemCommandService {
    Optional<OdontogramItem> handle(UpdateOdontogramItemCommand command);
}
