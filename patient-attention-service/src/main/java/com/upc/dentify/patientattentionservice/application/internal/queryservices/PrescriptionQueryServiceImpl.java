package com.upc.dentify.patientattentionservice.application.internal.queryservices;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.Prescription;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetPrescriptionByIdQuery;
import com.upc.dentify.patientattentionservice.domain.services.PrescriptionQueryService;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrescriptionQueryServiceImpl implements PrescriptionQueryService {
    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionQueryServiceImpl(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    @Override
    public Optional<Prescription> handle(GetPrescriptionByIdQuery query) {
        return prescriptionRepository.findById(query.id());
    }
}
