package com.upc.dentify.patientattentionservice.domain.services;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.AllergiasMedications;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetAllergiasMedicationsByIdQuery;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetAllergiasMedicationsByClinicalRecordIdQuery;

import java.util.List;
import java.util.Optional;

public interface AllergiasMedicationsQueryService {
    Optional<AllergiasMedications> handle(GetAllergiasMedicationsByIdQuery query);
    List<AllergiasMedications> handle(GetAllergiasMedicationsByClinicalRecordIdQuery query);
}
