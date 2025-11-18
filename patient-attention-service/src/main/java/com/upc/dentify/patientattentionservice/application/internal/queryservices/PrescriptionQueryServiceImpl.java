package com.upc.dentify.patientattentionservice.application.internal.queryservices;

import com.upc.dentify.patientattentionservice.application.internal.outboundservices.ExternalAppointmentService;
import com.upc.dentify.patientattentionservice.domain.model.aggregates.Prescription;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetPrescriptionByAppointmentIdQuery;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetPrescriptionByIdQuery;
import com.upc.dentify.patientattentionservice.domain.services.PrescriptionQueryService;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.ClinicalRecordEntryRepository;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrescriptionQueryServiceImpl implements PrescriptionQueryService {
    private final PrescriptionRepository prescriptionRepository;
    private final ClinicalRecordEntryRepository clinicalRecordEntryRepository;
    private final ExternalAppointmentService externalAppointmentService;

    public PrescriptionQueryServiceImpl(PrescriptionRepository prescriptionRepository,
                                        ExternalAppointmentService externalAppointmentService,
                                        ClinicalRecordEntryRepository clinicalRecordEntryRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.externalAppointmentService = externalAppointmentService;
        this.clinicalRecordEntryRepository = clinicalRecordEntryRepository;
    }

    @Override
    public Optional<Prescription> handle(GetPrescriptionByIdQuery query) {
        return prescriptionRepository.findById(query.id());
    }

    @Override
    public Optional<Prescription> handle(GetPrescriptionByAppointmentIdQuery query) {
        if (!externalAppointmentService.existsById(query.appointmentId())) {
            throw new IllegalArgumentException("Appointment id not found");
        }
        if (!clinicalRecordEntryRepository.existsByAppointmentId(query.appointmentId())) {
            throw new IllegalArgumentException("Appointment id " + query.appointmentId() + " does not exist");
        }
        var entry = clinicalRecordEntryRepository.findByAppointmentId(query.appointmentId());
        return prescriptionRepository.findById(entry.get().getPrescription().getId());
    }
}
