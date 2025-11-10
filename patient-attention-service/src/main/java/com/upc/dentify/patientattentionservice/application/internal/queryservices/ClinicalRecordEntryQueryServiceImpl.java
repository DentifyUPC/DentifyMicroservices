package com.upc.dentify.patientattentionservice.application.internal.queryservices;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.ClinicalRecordEntries;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetAllClinicalRecordEntriesByClinicalRecordIdQuery;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetClinicalRecordEntryByIdQuery;
import com.upc.dentify.patientattentionservice.domain.services.ClinicalRecordEntryQueryService;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.ClinicalRecordEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClinicalRecordEntryQueryServiceImpl implements ClinicalRecordEntryQueryService {
    private final ClinicalRecordEntryRepository clinicalRecordEntryRepository;

    public ClinicalRecordEntryQueryServiceImpl(ClinicalRecordEntryRepository clinicalRecordEntryRepository) {
        this.clinicalRecordEntryRepository = clinicalRecordEntryRepository;
    }

    @Override
    public List<ClinicalRecordEntries> handle(GetAllClinicalRecordEntriesByClinicalRecordIdQuery query) {
        return clinicalRecordEntryRepository.findAllByClinicalRecords_Id(query.clinicalRecordId());
    }

    @Override
    public Optional<ClinicalRecordEntries> handle(GetClinicalRecordEntryByIdQuery query) {
        return clinicalRecordEntryRepository.findById(query.id());
    }
}
