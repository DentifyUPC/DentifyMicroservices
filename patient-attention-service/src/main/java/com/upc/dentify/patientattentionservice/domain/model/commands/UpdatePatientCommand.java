package com.upc.dentify.patientattentionservice.domain.model.commands;

import com.upc.dentify.patientattentionservice.domain.model.valueobjects.Gender;

public record UpdatePatientCommand(
        Long patientId,
        Gender gender,
        String street,
        String district,
        String department,
        String province,
        String phoneNumber
) {
}
