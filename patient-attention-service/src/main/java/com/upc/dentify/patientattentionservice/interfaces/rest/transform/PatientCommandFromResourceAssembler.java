package com.upc.dentify.patientattentionservice.interfaces.rest.transform;

import com.upc.dentify.patientattentionservice.domain.model.commands.UpdatePatientCommand;
import com.upc.dentify.patientattentionservice.domain.model.valueobjects.Gender;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.UpdatePatientRequestResource;

public class PatientCommandFromResourceAssembler {
    public static UpdatePatientCommand toCommand(Long patientId, UpdatePatientRequestResource request) {
        return new UpdatePatientCommand(
                patientId,
                Gender.valueOf(request.gender().toUpperCase()),
                request.street(),
                request.district(),
                request.department(),
                request.province(),
                request.phoneNumber()
        );
    }
}
