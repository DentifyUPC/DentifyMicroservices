package com.upc.dentify.patientattentionservice.application.internal.queryservices;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.ClinicalRecords;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetClinicalRecordByPatientIdQuery;
import com.upc.dentify.patientattentionservice.domain.services.ClinicalRecordQueryService;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.ClinicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClinicalRecordQueryServiceImpl implements ClinicalRecordQueryService {

    private final ClinicalRecordRepository clinicalRecordRepository;

    public ClinicalRecordQueryServiceImpl(ClinicalRecordRepository clinicalRecordRepository) {
        this.clinicalRecordRepository = clinicalRecordRepository;
    }

    @Override
    public Optional<ClinicalRecords> handle(GetClinicalRecordByPatientIdQuery query) {
        return clinicalRecordRepository.findByPatientId(query.patientId());
    }
}
