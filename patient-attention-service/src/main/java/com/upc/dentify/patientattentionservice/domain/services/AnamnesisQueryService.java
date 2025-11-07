package com.upc.dentify.patientattentionservice.domain.services;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.Anamnesis;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetAnamnesisByIdQuery;

import java.util.Optional;

public interface AnamnesisQueryService {
    Optional<Anamnesis> handle(GetAnamnesisByIdQuery query);
}
