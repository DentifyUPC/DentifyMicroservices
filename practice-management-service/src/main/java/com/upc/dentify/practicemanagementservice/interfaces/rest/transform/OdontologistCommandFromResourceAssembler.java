package com.upc.dentify.practicemanagementservice.interfaces.rest.transform;

import com.upc.dentify.practicemanagementservice.domain.model.commands.UpdateOdontologistCommand;
import com.upc.dentify.practicemanagementservice.domain.model.valueobjects.Gender;
import com.upc.dentify.practicemanagementservice.interfaces.rest.resources.UpdateOdontologistRequestResource;

public class OdontologistCommandFromResourceAssembler {
    public static UpdateOdontologistCommand toCommand(Long odontologistId, UpdateOdontologistRequestResource request) {
        return new UpdateOdontologistCommand(
                odontologistId,
                Gender.valueOf(request.gender().toUpperCase()),
                request.street(),
                request.district(),
                request.department(),
                request.province(),
                request.phoneNumber(),
                request.professionalLicenseNumber(),
                request.specialtyRegistrationNumber(),
                request.specialty(),
                request.serviceId(),
                request.shiftName(),
                request.isActive()
        );
    }
}