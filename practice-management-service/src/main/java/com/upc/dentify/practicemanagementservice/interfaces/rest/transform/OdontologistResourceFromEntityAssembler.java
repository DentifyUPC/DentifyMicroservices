package com.upc.dentify.practicemanagementservice.interfaces.rest.transform;

import com.upc.dentify.practicemanagementservice.domain.model.aggregates.Odontologist;
import com.upc.dentify.practicemanagementservice.interfaces.rest.resources.OdontologistResource;

public class OdontologistResourceFromEntityAssembler {
    public static OdontologistResource toResource(Odontologist entity) {
        return new OdontologistResource(
                entity.getId(),
                entity.getPersonName().firstName(),
                entity.getPersonName().lastName(),
                entity.getBirthDate().birthDate(),
                entity.getEmail().email(),
                entity.getGender() != null ? entity.getGender().name() : null,
                entity.getAddress() != null ? entity.getAddress().street() : null,
                entity.getAddress() != null ? entity.getAddress().district() : null,
                entity.getAddress() != null ? entity.getAddress().department() : null,
                entity.getAddress() != null ? entity.getAddress().province() : null,
                entity.getPhoneNumber() != null ? entity.getPhoneNumber().phoneNumber() : null,
                entity.getAge(),
                entity.getClinicId(),
                entity.getProfessionalLicenseNumber(),
                entity.getSpecialtyRegistrationNumber(),
                entity.getSpecialty(),
                entity.getUserId(),
                entity.getServiceId(),
                entity.getShiftName() != null ? entity.getShiftName() : null,
                entity.isActive()
        );
    }
}