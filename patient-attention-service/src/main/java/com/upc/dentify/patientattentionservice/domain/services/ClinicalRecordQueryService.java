package com.upc.dentify.patientattentionservice.domain.services;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.ClinicalRecords;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetClinicalRecordByPatientIdQuery;

import java.util.Optional;

public interface ClinicalRecordQueryService {
    Optional<ClinicalRecords> handle(GetClinicalRecordByPatientIdQuery query);
}
