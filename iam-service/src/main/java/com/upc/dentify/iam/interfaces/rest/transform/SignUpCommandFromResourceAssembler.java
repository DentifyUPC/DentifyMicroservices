package com.upc.dentify.iam.interfaces.rest.transform;

import com.upc.dentify.iam.domain.model.commands.SignUpCommand;
import com.upc.dentify.iam.interfaces.rest.resources.RegisterRequestResource;

public class SignUpCommandFromResourceAssembler {
    public static SignUpCommand toCommandFromResource(RegisterRequestResource resource) {
        return new SignUpCommand(
                resource.firstName(),
                resource.lastName(),
                resource.username(),
                resource.password(),
                resource.birthDate(),
                resource.email(),
                resource.identificationTypeId(),
                resource.roleId(),
                resource.clinicId()
        );
    }
}
