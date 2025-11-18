package com.upc.dentify.patientattentionservice.domain.services;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.Prescription;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetPrescriptionByAppointmentIdQuery;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetPrescriptionByIdQuery;

import java.util.Optional;

public interface PrescriptionQueryService {
    Optional<Prescription> handle(GetPrescriptionByIdQuery query);
    Optional<Prescription> handle(GetPrescriptionByAppointmentIdQuery query);
}
