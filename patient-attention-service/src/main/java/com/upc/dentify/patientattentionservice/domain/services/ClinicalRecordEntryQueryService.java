package com.upc.dentify.patientattentionservice.domain.services;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.ClinicalRecordEntries;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetAllClinicalRecordEntriesByClinicalRecordIdQuery;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetClinicalRecordEntryByIdQuery;

import java.util.List;
import java.util.Optional;

public interface ClinicalRecordEntryQueryService {
    Optional<ClinicalRecordEntries> handle(GetClinicalRecordEntryByIdQuery query);
    List<ClinicalRecordEntries> handle(GetAllClinicalRecordEntriesByClinicalRecordIdQuery query);
}
