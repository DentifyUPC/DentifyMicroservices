package com.upc.dentify.patientattentionservice.application.acl;

import com.upc.dentify.patientattentionservice.domain.model.queries.GetAllPatientsByClinicId;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetPatientById;
import com.upc.dentify.patientattentionservice.domain.services.PatientQueryService;
import com.upc.dentify.patientattentionservice.interfaces.acl.PatientContextFacade;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.PatientExternalResource;
import com.upc.dentify.patientattentionservice.interfaces.rest.transform.PatientExternalResourceFromEntityAssembler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientContextFacadeImpl implements PatientContextFacade {
    private final PatientQueryService patientQueryService;

    public PatientContextFacadeImpl(PatientQueryService patientQueryService) {
        this.patientQueryService = patientQueryService;
    }

    @Override
    public List<PatientExternalResource> getAllPatientsByClinicId(Long clinicId) {
        var query = new GetAllPatientsByClinicId(clinicId);
        var patients = patientQueryService.handle(query);
        return patients.stream()
                .map(PatientExternalResourceFromEntityAssembler::fromEntity)
                .toList();
    }

    @Override
    public Optional<PatientExternalResource> getPatientById(Long id) {
        var query = new GetPatientById(id);
        var patient = patientQueryService.handle(query);
        return patient.map(PatientExternalResourceFromEntityAssembler::fromEntity);
    }
}
