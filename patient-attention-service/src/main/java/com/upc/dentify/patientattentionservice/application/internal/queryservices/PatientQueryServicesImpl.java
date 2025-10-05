package com.upc.dentify.patientattentionservice.application.internal.queryservices;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.Patient;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetAllPatientsByClinicId;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetPatientById;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetPatientByUserId;
import com.upc.dentify.patientattentionservice.domain.services.PatientQueryService;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientQueryServicesImpl implements PatientQueryService {
    private final PatientRepository patientRepository;

    public PatientQueryServicesImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public List<Patient> handle(GetAllPatientsByClinicId query) {
        return patientRepository.findAllByClinicId(query.clinicId());
    }

    @Override
    public Optional<Patient> handle(GetPatientByUserId query) {
        return patientRepository.findByUserId(query.userId());
    }

    @Override
    public Optional<Patient> handle(GetPatientById query) {
        return patientRepository.findById(query.id());
    }
}
