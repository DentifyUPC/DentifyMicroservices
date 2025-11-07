package com.upc.dentify.patientattentionservice.interfaces.acl;

import com.upc.dentify.patientattentionservice.interfaces.rest.resources.PatientExternalResource;

import java.util.List;
import java.util.Optional;

public interface PatientContextFacade {
    List<PatientExternalResource> getAllPatientsByClinicId(Long clinicId);
    Optional<PatientExternalResource> getPatientById(Long id);
}
